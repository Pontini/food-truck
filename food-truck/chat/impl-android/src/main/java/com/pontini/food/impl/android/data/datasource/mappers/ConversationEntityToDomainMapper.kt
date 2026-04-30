package com.pontini.food.impl.android.data.datasource.mappers

import com.pontini.food.core.mapper.Mapper
import com.pontini.food.domain.models.Conversation
import com.pontini.food.impl.android.data.model.room.ConversationEntity

class ConversationEntityToDomainMapper : Mapper<ConversationEntity, Conversation> {
    override fun map(from: ConversationEntity): Conversation {
        return Conversation(
            id = from.id,
            name = from.name,
            lastMessage = from.lastMessage,
            timestamp = from.timestamp,
        )
    }
}