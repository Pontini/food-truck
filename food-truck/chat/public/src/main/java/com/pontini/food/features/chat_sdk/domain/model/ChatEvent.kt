package com.pontini.food.features.chat_sdk.domain.model

import com.pontini.food.features.conversations.Message

sealed class ChatEvent {
    data class MessageReceived(val message: Message) : ChatEvent()
}