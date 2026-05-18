package com.pontini.food.domain.repositories

import com.pontini.food.domain.models.ConversationResult
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversations(): Flow<ConversationResult>
}