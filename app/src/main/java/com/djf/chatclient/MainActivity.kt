package com.djf.chatclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djf.chatclient.presentation.ChatViewModel
import com.djf.chatclient.screens.ChannelActivity
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
        enableEdgeToEdge()
        setContent {
            ChatTheme {
                Scaffold(modifier = Modifier.fillMaxSize())
                { innerPadding ->
                    val clientInitialisationState by viewModel.chatClient.clientState.initializationState.collectAsStateWithLifecycle()
                    when (clientInitialisationState) {
                        InitializationState.COMPLETE -> {
                            viewModel.createInitialChannel()
                            Box(modifier = Modifier.padding(innerPadding))
                            {
                                ChannelsScreen(
                                    title = stringResource(id = R.string.app_name),
                                    searchMode = SearchMode.Channels,
                                    onChannelClick = { channel ->
                                        launchMessages(channel)
                                    },
                                    onBackPressed = { finish() }
                                )
                            }
                        }

                        InitializationState.INITIALIZING -> {
                            Text(text = "Initialising...")
                        }

                        InitializationState.NOT_INITIALIZED -> {
                            LoginScreen(viewModel::connectClient)
                        }
                    }
                }
            }
        }
    }

    private fun launchMessages(channel: Channel) {
        startActivity(ChannelActivity.getIntent(this, channel.cid))
    }
}


@Preview
@Composable
fun MainScreen() {
    Text("rad")
}

@Composable
fun LoginScreen(onClick: (name: String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("name") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(name, { name = it })
        Button(onClick = {
            onClick(name)
        }) {
            Text("Login")
        }
    }

}

