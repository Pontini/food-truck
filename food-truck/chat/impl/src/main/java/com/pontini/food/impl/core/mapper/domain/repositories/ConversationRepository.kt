package com.pontini.food.impl.core.mapper.domain.repositories

import com.pontini.food.impl.core.mapper.domain.model.ConversationResult
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversations(): Flow<ConversationResult>
}