package com.pontini.food.impl.features.conversations.data.datasource

import com.pontini.food.impl.features.conversations.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationLocalDataSource {
    fun observe(): Flow<List<Conversation>>
    suspend fun save(list: List<Conversation>)
}