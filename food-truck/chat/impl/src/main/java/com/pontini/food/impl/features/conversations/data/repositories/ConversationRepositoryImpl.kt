package com.pontini.food.impl.features.conversations.data.repositories

import com.pontini.food.impl.features.conversations.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.features.conversations.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.features.conversations.domain.model.Conversation
import com.pontini.food.impl.features.conversations.domain.model.ConversationResult
import com.pontini.food.impl.features.conversations.domain.model.Source
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ConversationRepositoryImpl(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource
) : ConversationRepository {

    override fun getConversations(): Flow<ConversationResult> = channelFlow {

        // 🔥 1. Loading inicial
        send(ConversationResult.Loading)

        // 🔥 2. Cache
        val job = launch {
            local.observe().collect { list ->
                send(
                    ConversationResult.Success(
                        data = list,
                        source = Source.CACHE
                    )
                )
            }
        }

        // 🔥 3. Remote
        launch {
            try {
                delay(2000)

                val remoteData = remote.getLastMessages()
                local.save(remoteData)

                send(
                    ConversationResult.Success(
                        data = remoteData,
                        source = Source.REMOTE
                    )
                )

            } catch (e: Exception) {
                send(
                    ConversationResult.Error(
                        message = e.message ?: "Erro"
                    )
                )
            }
        }

        awaitClose { job.cancel() }
    }

}