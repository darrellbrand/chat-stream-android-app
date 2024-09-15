package com.djf.chatclient.presentation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import com.djf.chatclient.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 2 - Add the MessagesScreen to your UI
        enableEdgeToEdge()
        setContent {
            UserScreen(launchChannel = ::launchMain, onBackPress = onBackPress)
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


    private val onBackPress = { onBackPressedDispatcher.onBackPressed() }
}