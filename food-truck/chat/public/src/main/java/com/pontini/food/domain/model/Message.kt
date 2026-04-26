package com.pontini.food.domain.model

data class Message(
    val id: String,
    val conversationId: String,
    val text: String,
    val senderId: String,
    val senderName: String,
    val timestamp: Long,
    val typeMessage: TypeMessage
)

enum class TypeMessage {
    SENT, RECEIVED
}