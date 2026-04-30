package com.pontini.food.impl.android.features.chat.data.datasource.impl.local

import com.pontini.food.impl.android.features.chat.data.datasource.impl.local.room.MessageDao
import com.pontini.food.impl.android.features.chat.data.model.room.MessageEntity
import com.pontini.food.impl.data.model.request.SendMessageRequest
import com.pontini.food.impl.data.datasource.ChatLocalDataSource
import com.pontini.food.impl.core.mapper.domain.model.excpetion.FailedSaveMessageException
import com.pontini.food.core.mapper.Mapper
import com.pontini.food.features.domain.models.Message
import com.pontini.food.features.domain.models.TypeMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatLocalDataSourceImpl(
    private val dao: MessageDao,
    private val messageToEntityMapper: Mapper<SendMessageRequest, MessageEntity>
) : ChatLocalDataSource {
    override fun getMessages(conversationId: String): Flow<List<Message>> {
        return dao.observe(conversationId).map { entities ->
            entities.map { entity ->
                Message(
                    id = entity.id,
                    conversationId = entity.conversationId,
                    text = entity.message,
                    senderName = entity.senderName,
                    timestamp = entity.timestamp,
                    typeMessage = if (entity.isSent) TypeMessage.SENT else TypeMessage.RECEIVED
                )
            }
        }
    }

    override suspend fun insert(sendMessageRequest: SendMessageRequest) {
        try {
            val messageEntity = messageToEntityMapper.map(sendMessageRequest)
            dao.insert(messageEntity)
        }catch (e: Exception) {
            e.printStackTrace()
            throw FailedSaveMessageException("Failed to save message: ${e.message}")
        }

    }
}