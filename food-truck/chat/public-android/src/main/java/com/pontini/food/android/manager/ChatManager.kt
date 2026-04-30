package com.pontini.food.android.manager

import com.pontini.food.domain.models.ConnectionStatus
import com.pontini.food.features.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatManager{
    suspend fun sendMessage(message: String,conversationId:String)
    fun getMessagesById(conversationId: String): Flow<List<Message>>
    fun getConnection(): Flow<ConnectionStatus>
}