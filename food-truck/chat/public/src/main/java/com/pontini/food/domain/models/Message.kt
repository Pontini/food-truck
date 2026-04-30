package com.pontini.food.features.domain.models

data class Message(
    val id: String,
    val conversationId: String,
    val text: String,
    val senderName: String,
    val timestamp: Long,
    val typeMessage: TypeMessage
)

enum class TypeMessage {
    SENT, RECEIVED
}