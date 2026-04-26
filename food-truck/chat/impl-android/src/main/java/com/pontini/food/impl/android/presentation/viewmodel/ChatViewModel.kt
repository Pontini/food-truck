package com.pontini.food.impl.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.pontini.food.android.manager.ChatManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatManager: ChatManager,
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    init {
        observeMessages()
    }

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> sendMessage(intent.text)
            is ChatIntent.Connect -> connect()
        }
    }

    private fun connect() {

        _state.update { it.copy(isConnecting = true) }
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = true
            )
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            chatManager.observeMessages().collect{
                println("📩 [ViewModel] Nova lista de mensagens (${it.size} msgs)")
                println("➡️ Última: ${it.lastOrNull()?.text}")
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            chatManager.sendMessage(text)
        }
    }
}