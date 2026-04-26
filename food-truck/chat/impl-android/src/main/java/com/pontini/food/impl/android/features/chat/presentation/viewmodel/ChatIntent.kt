package com.pontini.food.impl.android.features.chat.presentation.viewmodel

sealed interface ChatIntent {

    data class SendMessage(val text: String) : ChatIntent

    data class Connect(val conversationId:String) : ChatIntent
}