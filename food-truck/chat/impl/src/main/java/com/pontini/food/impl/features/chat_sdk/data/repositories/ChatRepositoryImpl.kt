package com.pontini.food.impl.features.chat_sdk.data.repositories

import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import com.pontini.food.domain.model.TypeMessage
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatLocalDataSource
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.features.chat_sdk.data.model.request.SendMessageRequest
import com.pontini.food.impl.features.chat_sdk.domain.repositories.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

class ChatRepositoryImpl(
    private val local: ChatLocalDataSource,
    private val remote: ChatRemoteDataSource,
) : ChatRepository {

    override suspend fun connect() {
        remote.connect()
    }

    override suspend fun sendMessage(message: String, conversationId: String) {
        val sendMessageRequest = SendMessageRequest(
            conversationId = conversationId,
            message = message,
            isSent = true
        )

        local.insert(sendMessageRequest)
        remote.send(message, conversationId)
    }

    override fun getMessagesById(conversationId: String): Flow<List<Message>> {
        remote.events
            .mapNotNull { event ->
                (event as? ConnectionState.Data.MessageReceived)?.message
            }
            .onEach { message ->
                local.insert(
                    SendMessageRequest(
                        conversationId = conversationId,
                        message = message.text,
                        isSent = message.typeMessage == TypeMessage.SENT
                    )
                )
            }
            .launchIn(CoroutineScope(Dispatchers.IO))

        return local.observeMessages(conversationId)
    }

    override fun getConnection(): Flow<ConnectionState> {
        return remote.events
            .filterIsInstance<ConnectionState.Connection>()
    }
}