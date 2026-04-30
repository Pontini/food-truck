package com.pontini.food.impl.data.datasource

import com.pontini.food.impl.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationLocalDataSource {
    fun getMessages(): Flow<List<Conversation>>
    suspend fun save(list: List<Conversation>)
}