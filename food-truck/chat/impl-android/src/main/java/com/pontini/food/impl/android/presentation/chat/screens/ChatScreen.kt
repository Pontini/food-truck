package com.pontini.food.impl.android.presentation.chat.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.food.truck.impl.android.R
import com.pontini.food.features.domain.models.Message
import com.pontini.food.features.domain.models.TypeMessage
import com.pontini.food.impl.android.presentation.chat.viewmodel.ChatIntent
import com.pontini.food.impl.android.presentation.chat.viewmodel.ChatState
import com.pontini.food.impl.android.presentation.chat.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatScreen(
    conversationId: String,
    name: String,
    viewModel: ChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(conversationId) {
        viewModel.dispatcher(ChatIntent.Init(conversationId))
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    ChatScreenContent(
        name = name,
        state = state,
        onSendMessage = { text -> viewModel.dispatcher(ChatIntent.SendMessage(text)) }
    )
}

@Composable
fun ChatScreenContent(
    name: String,
    state: ChatState,
    onSendMessage: (String) -> Unit
) {
    val listState = rememberLazyListState()

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
                        placeholder = { Text(stringResource(R.string.chat_message_placeholder)) },
                        shape = MaterialTheme.shapes.large
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                onSendMessage(text)
                                text = ""
                            }
                        },
                        shape = MaterialTheme.shapes.large
                    ) {
                        Text(stringResource(R.string.chat_send_button))
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
        isConnecting -> stringResource(R.string.chat_status_connecting) to MaterialTheme.colorScheme.surfaceVariant
        isConnected -> stringResource(R.string.status_online) to MaterialTheme.colorScheme.primary
        else -> stringResource(R.string.chat_status_offline) to MaterialTheme.colorScheme.error
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

private val PREVIEW_MESSAGES = listOf(
    Message(
        id = "1",
        conversationId = "1",
        text = "Oi! Qual o horário de vocês hoje?",
        senderName = "Cliente",
        timestamp = System.currentTimeMillis(),
        typeMessage = TypeMessage.RECEIVED
    ),
    Message(
        id = "2",
        conversationId = "1",
        text = "Abrimos às 18h, te esperamos!",
        senderName = "Me",
        timestamp = System.currentTimeMillis(),
        typeMessage = TypeMessage.SENT
    )
)

private class ChatStatePreviewProvider : PreviewParameterProvider<ChatState> {
    override val values = sequenceOf(
        ChatState(messages = PREVIEW_MESSAGES, isConnected = true),
        ChatState(isConnecting = true),
        ChatState(messages = PREVIEW_MESSAGES, isConnected = false)
    )
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenPreview(
    @PreviewParameter(ChatStatePreviewProvider::class) state: ChatState
) {
    MaterialTheme {
        ChatScreenContent(
            name = "Food Truck do Zé",
            state = state,
            onSendMessage = {}
        )
    }
}
