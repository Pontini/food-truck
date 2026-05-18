package com.pontini.food.impl.data.datasource

import com.pontini.food.domain.models.Conversation


interface ConversationRemoteDataSource {
    suspend fun getLastMessages(): List<Conversation>
}