package com.djf.chatclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.djf.chatclient.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.Filters
import io.getstream.chat.android.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class UsersViewModel @Inject constructor(
    private val chatClient: ChatClient,
) : ViewModel() {

    private val _userList: MutableStateFlow<List<User>> = MutableStateFlow(emptyList())
    var userList = _userList.asStateFlow()

    fun getUsers() {
        // No filters, so this will query all users
        val filter = Filters.neutral()  // Neutral filter queries all users
        // Query users with pagination (limit 30)
        val request = QueryUsersRequest(
            filter = filter, offset = 0, limit = 30, presence = true
            // querySort = sort// You can adjust the limit as needed
        )
        viewModelScope.launch {
            val queryPromise = withContext(Dispatchers.Default) {
                chatClient.queryUsers(request).execute()
            }
            val queryResult = queryPromise.getOrNull() ?: emptyList()
            _userList.value = queryResult
                .filter { it.online && it.id != chatClient.clientState.user.value?.id }
        }
    }

    fun createChannel(user: User) {
        val localUserId = chatClient.clientState.user.value?.id ?: "INVALID OR NULL"
        val otherUserId = user.id
        val memberList = listOf(user.id, localUserId)
        viewModelScope.launch {
            val createChannelRequest = withContext(Dispatchers.Default) {
                chatClient.createChannel(
                    "messaging", channelId = "",
                    memberIds = memberList, extraData = emptyMap()//mapOf("name" to otherUser)
                ).execute().getOrNull() ?: Channel()
            }
            withContext(Dispatchers.Default) {
                chatClient.addMembers(
                    memberIds = listOf(otherUserId, localUserId),
                    channelId = "",
                    channelType = "messaging"
                ).execute().getOrNull()
                val channelClient =
                    createChannelRequest.let {
                        chatClient.channel(
                            channelType = it.type,
                            channelId = it.id
                        )
                    }
                channelClient.watch().execute()
            }
        }
    }
}
