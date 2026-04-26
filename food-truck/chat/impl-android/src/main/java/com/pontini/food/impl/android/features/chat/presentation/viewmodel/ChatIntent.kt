package com.pontini.food.impl.android.features.chat.presentation.viewmodel

sealed interface ChatIntent {

    data class SendMessage(val text: String) : ChatIntent

    data object Connect : ChatIntent
}