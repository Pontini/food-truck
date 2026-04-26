package com.pontini.food.impl.android.features.chat.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.pontini.food.android.manager.ChatManager
import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.impl.android.core.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class ChatViewModel(
    private val chatManager: ChatManager
) : BaseViewModel<ChatIntent, ChatState>(ChatState()) {

    override fun dispatcher(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.Init -> onInit(intent.conversationId)
            is ChatIntent.SendMessage -> sendMessage(intent.text)
        }
    }

    private fun onInit(conversationId: String) {
        setState { it.copy(isConnecting = true) }
        observeMessages(conversationId)
        observeConnection()
        setState {
            it.copy(
                isConnecting = false,
                isConnected = true,
                conversationId = conversationId
            )
        }
    }

    private fun observeMessages(conversationId: String) {
        viewModelScope.launch {
            chatManager.getMessagesById(conversationId).collect { list ->
                println("📨 [ViewModel] Received messages: ${list.size}")
                setState {
                    it.copy(messages = list)
                }
            }
        }
    }

    private fun observeConnection() {
        viewModelScope.launch {
            chatManager.getConnectionStatus().collect { state ->
                when (state) {
                    is ConnectionState.Connection.Connecting -> {
                        setState {
                            it.copy(
                                isConnecting = true,
                                isConnected = false
                            )
                        }
                    }

                    is ConnectionState.Connection.Connected -> {
                        setState {
                            it.copy(
                                isConnecting = false,
                                isConnected = true
                            )
                        }
                    }

                    is ConnectionState.Connection.Error -> {
                        setState {
                            it.copy(
                                isConnecting = false,
                                isConnected = false,
                                error = state.message
                            )
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch {
            chatManager.sendMessage(text, state.value.conversationId)
        }
    }
}