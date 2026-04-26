package com.pontini.food.impl.android.features.chat.data.datasource.impl.local

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.android.features.chat.data.datasource.impl.local.room.MessageDao
import com.pontini.food.impl.android.features.chat.data.model.room.MessageEntity
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatLocalDataSource
import com.pontini.food.impl.features.chat_sdk.data.model.request.SendMessageRequest
import com.pontini.food.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatLocalDataSourceImpl(
    private val dao: MessageDao,
    private val messageToEntityMapper: Mapper<SendMessageRequest, MessageEntity>
) : ChatLocalDataSource {
    override fun observeMessages(conversationId: String): Flow<List<Message>> {
        return dao.observe().map { entities ->
            entities.filter { it.id == conversationId }.map { entity ->
                Message(
                    id = entity.id,
                    conversationId = entity.id,
                    text = entity.message,
                    senderId = "me",
                    senderName = "Você",
                    timestamp = entity.timestamp,
                    typeMessage = com.pontini.food.domain.model.TypeMessage.SENT
                )
            } // Fazer um mapper para converter MessageEntity para Message
        }
    }

    override suspend fun insert(sendMessageRequest: SendMessageRequest) {
        val messageEntity = messageToEntityMapper.map(sendMessageRequest)
        dao.insert(messageEntity)
    }
}