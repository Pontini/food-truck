package com.pontini.food.domain.repositories

import com.pontini.food.domain.models.ConnectionStatus
import com.pontini.food.features.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun connect()

    suspend fun sendMessage(message: String,conversationId:String)

    fun getMessagesById(conversationId: String): Flow<List<Message>>

    fun getConnection(): Flow<ConnectionStatus>
}