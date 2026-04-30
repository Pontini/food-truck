package com.pontini.food.domain.models

import com.pontini.food.features.domain.models.Message


sealed class ChatEvent {
    data class MessageReceived(val message: Message) : ChatEvent()
}