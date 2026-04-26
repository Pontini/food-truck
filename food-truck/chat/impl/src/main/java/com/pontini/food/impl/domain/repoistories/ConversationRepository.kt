package com.pontini.food.impl.domain.repoistories

import com.pontini.food.impl.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {

    fun observe(): Flow<List<Conversation>>

    suspend fun refresh()
}