package com.pontini.food.impl.android.features.chat.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pontini.food.impl.android.conversations.screens.ChatBubble
import com.pontini.food.impl.android.features.chat.presentation.viewmodel.ChatIntent
import com.pontini.food.impl.android.features.chat.presentation.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    conversationId: String,
    name: String,
    viewModel: ChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    LaunchedEffect(conversationId) {
        viewModel.dispatcher(ChatIntent.Init(conversationId))
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        topBar = {
            ChatConnectionBanner(
                isConnected = state.isConnected,
                isConnecting = state.isConnecting
            )

            Spacer(modifier = Modifier.width(8.dp))

        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(name)
            Spacer(modifier = Modifier.width(8.dp))

            LazyColumn(
                state = listState,
                reverseLayout = true,
                contentPadding = PaddingValues(
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 80.dp,
                    top = 8.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(
                    items = state.messages,
                    key = { it.id }
                ) { message ->
                    ChatBubble(message)
                }
            }

            var text by rememberSaveable { mutableStateOf("") }

            Surface(
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .imePadding()
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Digite uma mensagem...") },
                        shape = MaterialTheme.shapes.large
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                viewModel.dispatcher(
                                    ChatIntent.SendMessage(text)
                                )
                                text = ""
                            }
                        },
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text("Enviar")
                    }
                }
            }
        }
    }
}

@Composable
fun ChatConnectionBanner(
    isConnected: Boolean,
    isConnecting: Boolean
) {
    val (text, color) = when {
        isConnecting -> "Conectando..." to MaterialTheme.colorScheme.surfaceVariant
        isConnected -> "Online" to MaterialTheme.colorScheme.primary
        else -> "Offline" to MaterialTheme.colorScheme.error
    }

    Surface(
        color = color,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}