package com.pontini.food.impl.android.features.chat.data.mappers

import com.pontini.food.impl.android.features.chat.data.model.room.MessageEntity
import com.pontini.food.impl.features.chat_sdk.data.model.request.SendMessageRequest
import com.pontini.food.mapper.Mapper

class MessageToEntityMapper : Mapper<SendMessageRequest, MessageEntity> {
    override fun map(from: SendMessageRequest): MessageEntity {
        return MessageEntity(
            id = from.conversationId,
            conversationId = from.conversationId,
            message = from.message,
            senderName = "Você",
            timestamp = System.currentTimeMillis()
        )
    }
}