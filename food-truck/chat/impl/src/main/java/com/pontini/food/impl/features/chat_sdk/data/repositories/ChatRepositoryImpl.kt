package com.pontini.food.impl.features.chat_sdk.data.repositories

import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import com.pontini.food.domain.model.TypeMessage
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatLocalDataSource
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.features.chat_sdk.data.model.request.SendMessageRequest
import com.pontini.food.impl.features.chat_sdk.domain.repositories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

class ChatRepositoryImpl(
    private val local: ChatLocalDataSource,
    private val remote: ChatRemoteDataSource
) : ChatRepository {

    override suspend fun connect() {
        remote.connect()
    }

    override suspend fun sendMessage(message: String, conversationId: String) {
        val sendMessageRequest = SendMessageRequest(
            conversationId = conversationId,
            message = message
        )

        val msg = Message(
            id = generateId(),
            conversationId = conversationId,
            text = message,
            senderId = "me",
            senderName = "Você",
            timestamp = System.currentTimeMillis(),
            typeMessage = TypeMessage.SENT
        )

        local.insert(sendMessageRequest)
        remote.send(message, conversationId)
    }

    override fun observeMessages(conversationId: String): Flow<List<Message>> {
        remote.events
            .mapNotNull { event ->
                when (event) {
                    is ConnectionState.Data.MessageReceived -> event.message
                    else -> null
                }
            }
            .onEach { message ->
                val sendMessageRequest = SendMessageRequest(
                    conversationId = conversationId,
                    message = message.text
                )
                local.insert(sendMessageRequest)
            }

        return local.observeMessages(conversationId)
    }

    override fun observeConnection(): Flow<ConnectionState> {
        return remote.events
            .filterIsInstance<ConnectionState.Connection>()
    }

    private fun generateId(): String = java.util.UUID.randomUUID().toString()
}