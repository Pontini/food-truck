package com.pontini.food.impl.data.datasource

import com.pontini.food.impl.domain.model.Conversation

interface ConversationDataSource {
    suspend fun getLastMessages(): List<Conversation>
}