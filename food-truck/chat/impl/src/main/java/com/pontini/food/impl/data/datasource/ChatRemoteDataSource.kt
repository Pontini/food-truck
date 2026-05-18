package com.pontini.food.impl.core.data.datasource

import com.pontini.food.domain.models.ChatEvent
import com.pontini.food.domain.models.ConnectionStatus
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {

    suspend fun connect()

    suspend fun send(message: String, conversationId: String)

    val connectionStatus: Flow<ConnectionStatus>

    val chatEvent: Flow<ChatEvent>
}