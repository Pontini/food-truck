package com.pontini.food.impl.domain.repoistories

import com.pontini.food.impl.domain.model.Conversation

interface ConversationRepository {
    suspend fun getLastMessages(): List<Conversation>
}