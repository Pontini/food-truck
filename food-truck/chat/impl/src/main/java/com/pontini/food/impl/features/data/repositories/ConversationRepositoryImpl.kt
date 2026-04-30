package com.pontini.food.impl.features.data.repositories

import com.pontini.food.impl.features.conversations.domain.model.ConversationResult
import com.pontini.food.impl.features.conversations.domain.model.Source
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import com.pontini.food.impl.features.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.features.data.datasource.ConversationRemoteDataSource
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