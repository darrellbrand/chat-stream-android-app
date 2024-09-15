package com.djf.chatclient.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djf.chatclient.R
import com.djf.chatclient.presentation.ChatViewModel
import io.getstream.chat.android.compose.ui.channels.ChannelsScreen
import io.getstream.chat.android.compose.ui.channels.SearchMode
import io.getstream.chat.android.models.Channel
import io.getstream.chat.android.models.InitializationState


@Composable
fun MainScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    launchUsers: () -> Unit,
    launchMessages: (Channel) -> Unit,
    onBackPress: () -> Unit
) {
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
                        onBackPressed = { onBackPress() },
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