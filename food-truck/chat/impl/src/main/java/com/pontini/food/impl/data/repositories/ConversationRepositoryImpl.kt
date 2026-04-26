package com.pontini.food.impl.data.repositories

import com.pontini.food.impl.data.datasource.ConversationDataSource
import com.pontini.food.impl.domain.model.Conversation
import com.pontini.food.impl.domain.repoistories.ConversationRepository

class ConversationRepositoryImpl(private val conversationDataSource: ConversationDataSource) :
    ConversationRepository {
    override suspend fun getLastMessages(): List<Conversation> {
        return conversationDataSource.getLastMessages()
    }
}