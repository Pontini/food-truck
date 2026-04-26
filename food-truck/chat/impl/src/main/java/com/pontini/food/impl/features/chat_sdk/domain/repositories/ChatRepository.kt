package com.pontini.food.impl.features.chat_sdk.domain.repositories

import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun connect()

    suspend fun sendMessage(message: String,conversationId:String)

    fun observeMessages(conversationId: String): Flow<List<Message>>

    fun observeConnection(): Flow<ConnectionState>
}