package com.pontini.food.impl.domain.repositories

import com.pontini.food.features.chat_sdk.domain.model.ConnectionState
import com.pontini.food.features.conversations.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun connect()

    suspend fun sendMessage(message: String,conversationId:String)

    fun getMessagesById(conversationId: String): Flow<List<Message>>

    fun getConnection(): Flow<ConnectionState>
}