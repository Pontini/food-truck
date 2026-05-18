package com.pontini.food.impl.data.datasource

import com.pontini.food.domain.models.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationLocalDataSource {
    fun getMessages(): Flow<List<Conversation>>
    suspend fun save(list: List<Conversation>)
}