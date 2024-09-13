package com.djf.chatclient.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djf.chatclient.MainActivity
import com.djf.chatclient.presentation.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.User

@AndroidEntryPoint
class UsersActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2 - Add the MessagesScreen to your UI
        viewModel.getUsers()
        setContent {
            val userList by viewModel.userList.collectAsStateWithLifecycle()
            ChatTheme {
                UserList(userList, ::createChannelLaunchMain)
            }
        }
    }
    // 3 - Create an intent to start this Activity, with a given channelId
    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UsersActivity::class.java).apply {
            }
        }
    }
    private fun launchMain(){
        startActivity(MainActivity.getIntent(this))
    }
    private fun createChannelLaunchMain(user : User){
        viewModel.createChannel(user)
        launchMain()
    }
}