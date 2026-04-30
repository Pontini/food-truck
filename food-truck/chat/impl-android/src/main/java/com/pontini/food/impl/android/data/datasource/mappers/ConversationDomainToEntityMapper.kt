package com.pontini.food.impl.android.data.datasource.mappers

import com.pontini.food.core.mapper.Mapper
import com.pontini.food.domain.models.Conversation
import com.pontini.food.impl.android.data.model.room.ConversationEntity

class ConversationDomainToEntityMapper : Mapper<Conversation, ConversationEntity> {
    override fun map(from: Conversation): ConversationEntity {
        return ConversationEntity(
            id = from.id,
            name = from.name,
            lastMessage = from.lastMessage,
            timestamp = from.timestamp
        )
    }
}