package com.pontini.food.impl.android.presentation.chat.viewmodel

sealed interface ChatIntent {

    data class SendMessage(val text: String) : ChatIntent

    data class Init(val conversationId:String) : ChatIntent
}