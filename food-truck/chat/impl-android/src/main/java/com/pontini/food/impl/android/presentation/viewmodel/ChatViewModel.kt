package com.pontini.food.impl.android.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.android.manager.ChatManager
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatManager: ChatManager
) : BaseViewModel<ChatIntent, ChatState>(ChatState()) {

    init {
        observeMessages()
    }

    override fun dispatcher(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SendMessage -> sendMessage(intent.text)
            is ChatIntent.Connect -> connect()
        }
    }

    private fun connect() {
        setState { it.copy(isConnecting = true) }

        setState {
            it.copy(
                isConnecting = false,
                isConnected = true
            )
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            chatManager.observeMessages().collect { list ->
                println("📩 [ViewModel] Nova lista (${list.size} msgs)")
                println("➡️ Última: ${list.lastOrNull()?.text}")

                setState {
                    it.copy(
                        messages = list
                    )
                }
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            chatManager.sendMessage(text)
        }
    }
}