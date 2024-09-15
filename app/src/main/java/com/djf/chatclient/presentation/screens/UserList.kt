package com.djf.chatclient.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.djf.chatclient.R
import com.djf.chatclient.presentation.ChatViewModel
import io.getstream.chat.android.compose.ui.theme.ChatTheme
import io.getstream.chat.android.models.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    viewModel: ChatViewModel = hiltViewModel(), onBackPress: () -> Unit,
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

@Composable
fun UserList(userList: List<User>, block: (User) -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.undraw_chatting_lt27),
            "",
            modifier = Modifier.size(100.dp),
            Alignment.Center,
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(userList) {
                    UserItem(it, block)
                }
            }
        }
    }
}

@Composable
fun UserItem(user: User, block: (User) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { block(user) }
            .clip(RoundedCornerShape(5.dp))
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        AsyncImage(
            model = user.image,
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = user.id,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2 / 3f),
            textAlign = TextAlign.Left,
        )

    }
    HorizontalDivider()
}