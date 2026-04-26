package com.pontini.food.impl.data.datasource

import com.pontini.food.impl.features.conversations.domain.model.Conversation

interface ConversationRemoteDataSource {
    suspend fun getLastMessages(): List<Conversation>
}