package com.pontini.food.impl.data.model.request

data class SendMessageRequest(
    val message: String,
    val conversationId: String,
    val isSent: Boolean
)