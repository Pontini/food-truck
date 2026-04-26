package com.pontini.food.impl.features.conversations.domain.repoistories

import com.pontini.food.impl.features.conversations.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {

    fun observe(): Flow<List<Conversation>>

    suspend fun refresh()
}