package com.pontini.food.features.conversations

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