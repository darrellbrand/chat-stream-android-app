package com.djf.chatclient.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import io.getstream.chat.android.compose.ui.messages.MessagesScreen
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.compose.viewmodel.messages.MessagesViewModelFactory

class MessageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1 - Load the ID of the selected channel
        val channelId = intent.getStringExtra(KEY_CHANNEL_ID)!!
        // WindowCompat.setDecorFitsSystemWindows(window,false)
        enableEdgeToEdge()
        // 2 - Add the MessagesScreen to your UI
        setContent {
            ChatTheme {
                Scaffold(containerColor = Color.White) {
                    Box(
                        modifier = Modifier
                            .padding(it)
                            .imePadding()
                            .consumeWindowInsets(it)
                            .fillMaxSize()
                    ) {
                        MessagesScreen(
                            viewModelFactory = MessagesViewModelFactory(
                                context = LocalContext.current,
                                channelId = channelId,
                                messageLimit = 30,
                            ),
                            onBackPressed = { finish() },
                        )
                    }
                }
            }
        }
    }

    // 3 - Create an intent to start this Activity, with a given channelId
    companion object {
        private const val KEY_CHANNEL_ID = "channelId"
        fun getIntent(context: Context, channelId: String): Intent {
            return Intent(context, MessageActivity::class.java).apply {
                putExtra(KEY_CHANNEL_ID, channelId)
            }
        }
    }
}