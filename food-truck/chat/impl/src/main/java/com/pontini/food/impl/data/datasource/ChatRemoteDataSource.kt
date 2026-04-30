package com.pontini.food.impl.core.data.datasource

import com.pontini.food.features.chat_sdk.domain.model.ChatEvent
import com.pontini.food.features.chat_sdk.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {

    suspend fun connect()

    suspend fun send(message: String, conversationId: String)

    val connectionState: Flow<ConnectionState>

    val chatEvent: Flow<ChatEvent>
}