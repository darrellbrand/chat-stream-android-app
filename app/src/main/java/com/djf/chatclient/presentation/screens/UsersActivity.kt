package com.djf.chatclient.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.djf.chatclient.MainActivity
import com.djf.chatclient.R
import com.djf.chatclient.presentation.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.User

@AndroidEntryPoint
class UsersActivity : ComponentActivity() {
    private val viewModel: ChatViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2 - Add the MessagesScreen to your UI
        enableEdgeToEdge()
        viewModel.getUsers()
        setContent {
            val userList by viewModel.userList.collectAsStateWithLifecycle()
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
                        IconButton(onClick = { onBackPressedDispatcher.onBackPressed() }) {
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
    }

    // 3 - Create an intent to start this Activity, with a given channelId
    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UsersActivity::class.java).apply {}
        }
    }

    private fun launchMain() {
        startActivity(MainActivity.getIntent(this))
    }

    private fun createChannelLaunchMain(user: User) {
        viewModel.createChannel(user)
        launchMain()
    }
}