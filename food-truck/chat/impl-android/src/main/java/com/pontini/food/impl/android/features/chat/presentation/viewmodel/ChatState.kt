package com.pontini.food.impl.android.features.chat.presentation.viewmodel

import com.pontini.food.domain.model.Message

data class ChatState(
    val messages: List<Message> = emptyList(),
    val conversationId:String = "",
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val error: String? = null
)