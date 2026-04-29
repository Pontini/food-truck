package com.pontini.food.impl.android.features.conversations.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConversationsIntent
import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConversationsViewModel
import com.pontini.food.impl.features.conversations.domain.model.Conversation

@Composable
fun ConversationsScreen(
    viewModel: ConversationsViewModel = koinViewModel(),
    onOpenChat: (Conversation) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatcher(ConversationsIntent.Init)
    }

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
                        message = state.error ?: "Ocorreu um erro",
                        onRetry = {
                            viewModel.dispatcher(ConversationsIntent.Init)
                        }
                    )
                }

                state.conversations.isEmpty() -> {
                    Text(
                        text = "Sem dados offline",
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
                Text("Tentar novamente")
            }
        }
    }
}