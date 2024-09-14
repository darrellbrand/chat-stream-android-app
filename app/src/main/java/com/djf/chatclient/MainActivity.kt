package com.djf.chatclient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djf.chatclient.presentation.ChatViewModel
import com.djf.chatclient.presentation.screens.LoginScreen
import com.djf.chatclient.presentation.screens.MessageActivity
import com.djf.chatclient.presentation.screens.UsersActivity
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.SearchMode
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.InitializationState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            ChatTheme {

                Scaffold(modifier = Modifier.fillMaxSize(), containerColor = Color.White) {
                    val clientInitialisationState by viewModel.chatClient.clientState.initializationState.collectAsStateWithLifecycle()
                    val isNetworkError by viewModel.isNetworkError.collectAsStateWithLifecycle()
                    when (clientInitialisationState) {
                        InitializationState.COMPLETE -> {

                            Box(
                                modifier = Modifier
                                    .padding(it)
                                    .consumeWindowInsets(it)
                                    .safeDrawingPadding()
                            ) {
                                ChannelsScreen(title = stringResource(id = R.string.app_name),
                                    searchMode = SearchMode.Channels,
                                    onChannelClick = { channel ->
                                        launchMessages(channel)
                                    },
                                    onBackPressed = { finish() },
                                    onHeaderActionClick = { launchUsers() })
                            }
                        }

                        InitializationState.INITIALIZING -> {
                            Box(contentAlignment = Alignment.Center) {
                                LinearProgressIndicator(
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        InitializationState.NOT_INITIALIZED -> {
                            Box(
                                modifier = Modifier
                                    .imePadding()
                                    .consumeWindowInsets(it)
                            ) {
                                LoginScreen(
                                    viewModel::connectClient,
                                    isNetworkError
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    private fun launchMessages(channel: Channel) {
        startActivity(MessageActivity.getIntent(this, channel.cid))
    }

    private fun launchUsers() {
        startActivity(UsersActivity.getIntent(this))
    }

    // 3 - Create an intent to start this Activity, with a given channelId
    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}

