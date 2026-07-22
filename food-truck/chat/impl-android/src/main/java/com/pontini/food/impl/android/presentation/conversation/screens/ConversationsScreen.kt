package com.pontini.food.impl.android.presentation.conversation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.food.truck.impl.android.R
import com.pontini.food.domain.models.Conversation
import com.pontini.food.impl.android.presentation.conversation.viewmodel.ConnectionStatus
import com.pontini.food.impl.android.presentation.conversation.viewmodel.ConversationsIntent
import com.pontini.food.impl.android.presentation.conversation.viewmodel.ConversationsState
import com.pontini.food.impl.android.presentation.conversation.viewmodel.ConversationsViewModel

@Composable
fun ConversationsScreen(
    viewModel: ConversationsViewModel = koinViewModel(),
    onOpenChat: (Conversation) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatcher(ConversationsIntent.Init)
    }

    ConversationsScreenContent(
        state = state,
        onOpenChat = onOpenChat,
        onRetry = { viewModel.dispatcher(ConversationsIntent.Init) }
    )
}

@Composable
fun ConversationsScreenContent(
    state: ConversationsState,
    onOpenChat: (Conversation) -> Unit,
    onRetry: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        ConnectionBanner(status = state.connectionStatus)

        Box(modifier = Modifier.weight(1f)) {

            when {
                state.isLoading && state.conversations.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null && state.conversations.isEmpty() -> {
                    ErrorView(
                        message = state.error ?: stringResource(R.string.conversations_load_error),
                        onRetry = onRetry
                    )
                }

                state.conversations.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.conversations_empty_offline),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.conversations, key = { it.id }) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                onClick = { onOpenChat(conversation) }
                            )
                        }
                    }
                }
            }

            if (state.isLoading && state.conversations.isNotEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry_button))
            }
        }
    }
}

private val PREVIEW_CONVERSATIONS = listOf(
    Conversation(
        id = "1",
        name = "Food Truck do Zé",
        lastMessage = "Seu pedido saiu para entrega!",
        timestamp = System.currentTimeMillis()
    ),
    Conversation(
        id = "2",
        name = "Suporte",
        lastMessage = "Posso ajudar em algo mais?",
        timestamp = System.currentTimeMillis()
    )
)

private class ConversationsStatePreviewProvider : PreviewParameterProvider<ConversationsState> {
    override val values = sequenceOf(
        ConversationsState(isLoading = true),
        ConversationsState(
            conversations = PREVIEW_CONVERSATIONS,
            connectionStatus = ConnectionStatus.Online
        ),
        ConversationsState(connectionStatus = ConnectionStatus.OfflineNoData),
        ConversationsState(error = "Não foi possível carregar as conversas")
    )
}

@Preview(showBackground = true)
@Composable
private fun ConversationsScreenPreview(
    @PreviewParameter(ConversationsStatePreviewProvider::class) state: ConversationsState
) {
    MaterialTheme {
        Surface {
            ConversationsScreenContent(
                state = state,
                onOpenChat = {},
                onRetry = {}
            )
        }
    }
}
