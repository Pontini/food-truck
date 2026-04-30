package com.pontini.food.impl.domain.repositories

import com.pontini.food.impl.domain.model.ConversationResult
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversations(): Flow<ConversationResult>
}