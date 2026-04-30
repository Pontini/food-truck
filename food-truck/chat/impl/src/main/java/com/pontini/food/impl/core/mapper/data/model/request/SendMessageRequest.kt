package com.pontini.food.impl.core.mapper.data.model.request

data class SendMessageRequest(
    val message: String,
    val conversationId: String,
    val isSent: Boolean
)