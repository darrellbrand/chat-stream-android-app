package com.djf.chatclient.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djf.chatclient.presentation.UsersViewModel
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: UsersViewModel = hiltViewModel(), onBackPress: () -> Unit,
    launchChannel: () -> Unit
) {
    viewModel.getUsers()
    val userList by viewModel.userList.collectAsStateWithLifecycle()
    fun createChannelLaunchMain(user: User) {
        viewModel.createChannel(user)
        launchChannel()
    }
    ChatTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(
                    "Online Users",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center,
                )
            }, navigationIcon = {
                IconButton(onClick = { onBackPress() }) {
                    Icon(
                        imageVector = (Icons.AutoMirrored.Filled.ArrowBack),
                        contentDescription = ""
                    )
                }
            },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
                modifier = Modifier.shadow(elevation = 8.dp)
            )
        }) {
            Box(modifier = Modifier.padding(it)) {
                UserList(userList, ::createChannelLaunchMain)
            }
        }
    }

}