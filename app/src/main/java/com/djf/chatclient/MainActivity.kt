package com.djf.chatclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.djf.chatclient.presentation.screens.MainScreen
import com.djf.chatclient.presentation.screens.MessageActivity
import com.djf.chatclient.presentation.screens.UsersActivity
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.Channel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            ChatTheme {
                MainScreen(
                    launchMessages = ::launchMessages,
                    launchUsers = ::launchUsers,
                    onBackPress = ::onBackPress
                )
            }
        }
    }

    private fun launchMessages(channel: Channel) {
        startActivity(MessageActivity.getIntent(this, channel.cid))
    }

    private fun launchUsers() {
        startActivity(UsersActivity.getIntent(this))
    }

    private fun onBackPress() {
        onBackPressedDispatcher.onBackPressed()
    }

    // 3 - Create an intent to start this Activity, with a given channelId
    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

