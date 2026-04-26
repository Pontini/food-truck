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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pontini.food.impl.android.features.conversations.presentation.viewmodel.ConversationsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConversationsScreen(
    viewModel: ConversationsViewModel = koinViewModel(),
    onOpenChat: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box {

        LazyColumn {
            items(state.conversations, key = { it.id }) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onClick = { onOpenChat(conversation.id) }
                )
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        state.error?.let {
            ErrorView(
                message = it,
                onRetry = {
                }
            )
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