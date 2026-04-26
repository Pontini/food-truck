package com.pontini.food.impl.android.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pontini.food.android.manager.ChatManager
import com.pontini.food.impl.android.presentation.viewmodel.ChatIntent
import com.pontini.food.impl.android.presentation.viewmodel.ChatViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()

    Column {

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(state.messages) { message ->
                Text("${message.sender}: ${message.text}")
            }
        }

        Row {
            var text by remember { mutableStateOf("") }

            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    viewModel.onIntent(ChatIntent.SendMessage(text))
                    text = ""
                }
            ) {
                Text("Enviar")
            }
        }
    }
}