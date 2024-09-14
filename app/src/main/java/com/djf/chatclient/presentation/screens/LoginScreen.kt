package com.djf.chatclient.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.djf.chatclient.R


@Composable
fun LoginScreen(onClick: (name: String, image: String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("name") }
    var image by rememberSaveable { mutableStateOf("") }
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
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                image,
                { image = it },
                label = { Text("Image Url") },
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(onClick = {
                onClick(name, image)
            }) {
                Text("Login")
            }
        }
    }
}

