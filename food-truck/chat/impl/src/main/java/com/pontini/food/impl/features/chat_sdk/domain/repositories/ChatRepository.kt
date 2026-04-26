package com.pontini.food.impl.features.chat_sdk.domain.repositories

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun connect()

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<List<Message>>

    fun observeConnection(): Flow<ConnectionState>
}