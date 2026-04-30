package com.pontini.food.impl.data.repositories

import com.pontini.food.impl.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.domain.model.ConversationResult
import com.pontini.food.impl.domain.model.Source
import com.pontini.food.impl.domain.repositories.ConversationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConversationRepositoryImpl(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope,
    ) : ConversationRepository {

    override fun getConversations(): Flow<ConversationResult> {
        fetchRemote()

        return local.getMessages().map { cachedData ->
            ConversationResult(
                data = cachedData,
                source = Source.CACHE
            )
        }
    }

    private fun fetchRemote() {
        scope.launch(ioDispatcher)  {
            try {
                val remoteData = remote.getLastMessages()
                local.save(remoteData)
            } catch (_: Exception) {
            }
        }
    }
}