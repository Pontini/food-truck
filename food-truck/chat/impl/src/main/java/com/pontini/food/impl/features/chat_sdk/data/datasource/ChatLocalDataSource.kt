package com.pontini.food.impl.features.chat_sdk.data.datasource

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.data.model.request.SendMessageRequest
import kotlinx.coroutines.flow.Flow

interface ChatLocalDataSource {
    fun observeMessages(conversationId: String): Flow<List<Message>>
    suspend fun insert(sendMessageRequest: SendMessageRequest)
}