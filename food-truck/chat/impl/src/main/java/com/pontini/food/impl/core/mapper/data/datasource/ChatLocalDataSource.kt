package com.pontini.food.impl.core.mapper.data.datasource

import com.pontini.food.features.conversations.Message
import com.pontini.food.impl.core.mapper.data.model.request.SendMessageRequest
import kotlinx.coroutines.flow.Flow

interface ChatLocalDataSource {
    fun getMessages(conversationId: String): Flow<List<Message>>
    suspend fun insert(sendMessageRequest: SendMessageRequest)
}