package com.pontini.food.impl.android.features.conversations.data.datasource.mappers

import com.pontini.food.mapper.Mapper
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationEntity
import com.pontini.food.impl.features.conversations.domain.model.Conversation

class ConversationEntityToDomainMapper : Mapper<ConversationEntity, Conversation> {
    override fun map(from: ConversationEntity): Conversation {
        return Conversation(
            id = from.id,
            name = from.name,
            lastMessage = from.lastMessage,
            timestamp = from.timestamp
        )
    }
}