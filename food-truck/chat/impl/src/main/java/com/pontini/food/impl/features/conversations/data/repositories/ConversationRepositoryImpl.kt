package com.pontini.food.impl.features.conversations.data.repositories

import com.pontini.food.impl.features.conversations.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.features.conversations.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.features.conversations.domain.model.ConversationResult
import com.pontini.food.impl.features.conversations.domain.model.Source
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ConversationRepositoryImpl(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource,
) : ConversationRepository {

    override fun getConversations(): Flow<ConversationResult> = flow {
        emit(ConversationResult.Loading)
        emitAll(
            local.observe().map {
                ConversationResult.Success(it, Source.CACHE)
            }
        )
    }.onStart {
        try {
            val remoteData = remote.getLastMessages()
            local.save(remoteData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}