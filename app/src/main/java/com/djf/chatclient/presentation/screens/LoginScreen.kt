package com.djf.chatclient.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.djf.chatclient.R
import com.djf.chatclient.avatarList
import java.lang.Error


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LoginScreen(onClick: (name: String, image: String) -> Unit, isError: Boolean) {
    var name by rememberSaveable { mutableStateOf("name") }
    var image by rememberSaveable { mutableStateOf("https://cdn.iconscout.com/icon/free/png-512/free-avatar-icon-download-in-svg-png-gif-file-formats--user-man-avatars-flat-icons-pack-people-456323.png?f=webp&w=256") }
    Box {
        Image(
            painter = painterResource(R.drawable.large_triangles),
            contentDescription = "",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize(),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.undraw_chatting_lt27),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(200.dp)
            )
            TextField(
                name,
                { name = it },
                label = { Text("Username") },
                modifier = Modifier
                    .padding(5.dp)
                    .clip(
                        RoundedCornerShape(15.dp)
                    )
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    onClick(name, image)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(25.dp))
            FlowRow {
                avatarList.forEach {
                    Box(
                        modifier = Modifier
                            .clickable { image = it }
                            .padding(10.dp)
                            .clip(CircleShape)
                            .border(
                                width = 7.dp, color =
                                if (it == image) {
                                    Color.Blue
                                } else {
                                    Color.Transparent
                                }, shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = it,
                            contentDescription = "",
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
            MyAlertDialog(isError = isError) { onClick(name, image) }
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
        }
    }
}
@Composable
fun MyAlertDialog(isError: Boolean, onClick: () -> Unit) {
    if (isError) { // 2
        AlertDialog(
            onDismissRequest = {
                onClick()
            },
            title = { Text(text = "Server Connection Failed") },
            text = { Text(text = "Can't connect to server. Please try Again") },
            confirmButton = {
                Button(
                    onClick = {
                        onClick()
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White
                    )
                }
            }
        )
    }
}