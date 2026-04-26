package com.pontini.food.impl.features.conversations.data.repositories

import com.pontini.food.impl.features.conversations.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.features.conversations.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.features.conversations.domain.model.Conversation
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class ConversationRepositoryImpl(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource
) : ConversationRepository {

    override fun getConversations(): Flow<List<Conversation>> = flow {
        emitAll(local.observe())
    }.onStart {
        try {
            delay(2000)
            val remoteData = remote.getLastMessages()

            local.save(remoteData)

        } catch (e: Exception) {
           throw e
        }
    }

}