package com.pontini.food.impl.features.chat_sdk.data.model.request

data class SendMessageRequest(
    val message: String,
    val conversationId: String
)