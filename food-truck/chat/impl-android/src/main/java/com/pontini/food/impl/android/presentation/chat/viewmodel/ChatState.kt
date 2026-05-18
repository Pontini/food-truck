package com.pontini.food.impl.android.presentation.chat.viewmodel

import com.pontini.food.features.domain.models.Message


data class ChatState(
    val messages: List<Message> = emptyList(),
    val conversationId:String = "",
    val isConnecting: Boolean = false,
    val isConnected: Boolean = false,
    val error: String? = null
)