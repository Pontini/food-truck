package com.pontini.food.impl.features.conversations.domain.repoistories

import com.pontini.food.impl.features.conversations.domain.model.Conversation
import com.pontini.food.impl.features.conversations.domain.model.ConversationResult
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    fun getConversations(): Flow<ConversationResult>
}