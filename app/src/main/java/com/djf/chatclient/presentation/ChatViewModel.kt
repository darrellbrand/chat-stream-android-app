package com.djf.chatclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djf.chatclient.BuildConfig
import com.djf.chatclient.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import io.getstream.chat.android.models.querysort.QuerySorter
import io.getstream.result.Result
import io.getstream.result.call.Call
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val apiHeaderValue = BuildConfig.API_KEY_VALUE

@HiltViewModel
class ChatViewModel @Inject constructor(val apiService: ApiService, val chatClient: ChatClient) :
    ViewModel() {
    private val headerValue = BuildConfig.API_KEY_VALUE
    private val _name = MutableStateFlow("")
    var name = _name.asStateFlow()

    private val _token = MutableStateFlow("")
    var token = _token.asStateFlow()
    fun connectClient(name: String) {
        viewModelScope.launch {
            try {
                _token.value = apiService.initUser(headerValue = headerValue, email = name).token
                val user = User(
                    name = name,
                    id = name,
                    image = "https://bit.ly/2TIt8NR",
                )
                chatClient.connectUser(
                    user = user,
                    token = token.value
                ).enqueue()

            } catch (e: Exception) {
                println(e.printStackTrace())
            }
        }
    }

    fun createInitialChannel() {
        // No filters, so this will query all users
        val filter = Filters.neutral()  // Neutral filter queries all users
        // Query users with pagination (limit 30)
        val request = QueryUsersRequest(
            filter = filter,
            offset = 0,
            limit = 30,
            // querySort = sort// You can adjust the limit as needed
        )
        // Define the list of user IDs for the channel
        val members = emptyList<String>().toMutableList()
        viewModelScope.launch {
            val queryPromise: Deferred<Result<List<User>>> = async {
                chatClient.queryUsers(request).execute()
            }
            val queryResult: Result<List<User>> = queryPromise.await()

            val addMemberRequest: Deferred<Result<Channel>> = async {
                val retList = mutableListOf<String>()
                queryResult.getOrNull()?.forEach {
                    retList.add(it.id)
                }
                chatClient.addMembers(
                    memberIds = retList,
                    channelId = "general",
                    channelType = "messaging"
                ).execute()
            }
            val channelClient =
                chatClient.channel(channelType = "messaging", channelId = "general")
            val updatePartialRequest: Deferred<Result<Channel>> = async {

                channelClient.updatePartial(set = mapOf("name" to "general")).execute()
            }
            val addMemberResult = addMemberRequest.await()
            val updatePartialResult = updatePartialRequest.await()
            val watchChannelRequest: Deferred<Result<Channel>> = async {
                channelClient.watch().execute()
            }
            val watchChannelResult = watchChannelRequest.await()
        }

    }
}
