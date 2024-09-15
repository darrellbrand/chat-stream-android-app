package com.djf.chatclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djf.chatclient.BuildConfig
import com.djf.chatclient.remote.ApiService
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.models.Device
import io.getstream.chat.android.models.PushProvider
import io.getstream.chat.android.models.User
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
    private val apiService: ApiService,
    val chatClient: ChatClient,
) : ViewModel() {
    private val headerValue = BuildConfig.API_KEY_VALUE

    private val _isNetworkError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isNetworkError = _isNetworkError.asStateFlow()

    private fun setError(isError: Boolean) {
        _isNetworkError.value = isError
    }

    fun connectClient(name: String, image: String?) {
        viewModelScope.launch {
            try {
                coroutineScope {
                    val imageIn = if (image?.isNotEmpty() == true) {
                        image
                    } else {
                        "https://bit.ly/2TIt8NR"
                    }
                    val initUserResult = async {
                        apiService.initUser(
                            headerValue = headerValue, email = name
                        ).token
                    }
                    val streamToken = initUserResult.await()
                    val user = User(
                        name = name,
                        id = name,
                        image = imageIn,
                    )
                    chatClient.connectUser(
                        user = user, token = streamToken
                    ).execute()
                    getFireBaseToken()
                    setError(false)
                }
            } catch (e: Exception) {
                println(e.printStackTrace())
                setError(true)
            }
        }
    }

    private fun getFireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                println("CVM: got token")
                addDevice(it.result)
            } else {
                println("CVM: token fail ${it.exception}")
            }
        }
    }

    private fun addDevice(firebaseToken: String) {
        viewModelScope.launch {
            val addDeviceResult = async {
                chatClient.addDevice(
                    Device(
                        token = firebaseToken,
                        pushProvider = PushProvider.FIREBASE,
                        providerName = "basic-config",
                    )
                ).execute()
            }.await()
            if (addDeviceResult.isSuccess) {
                println("CVM: addDevice Success")
            } else {
                println("CVM: addDevice fail ${addDeviceResult.errorOrNull()}")
            }
        }

    }
}