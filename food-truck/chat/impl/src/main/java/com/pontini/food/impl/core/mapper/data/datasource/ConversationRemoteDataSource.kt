package com.pontini.food.impl.core.mapper.data.datasource

import com.pontini.food.impl.core.mapper.domain.model.Conversation


interface ConversationRemoteDataSource {
    suspend fun getLastMessages(): List<Conversation>
}