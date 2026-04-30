package com.pontini.food.impl.features.chat_sdk.data.datasource

import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {

    suspend fun connect()

    suspend fun send(message: String, conversationId: String)

    val connectionState: Flow<ConnectionState.Connection>

    val messages: Flow<Message>
}