package com.pontini.food.impl.data.repositories

import com.pontini.food.impl.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.domain.model.Conversation
import com.pontini.food.impl.domain.repoistories.ConversationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class ConversationRepositoryImpl(
    private val remote: ConversationRemoteDataSource,
    private val local: ConversationLocalDataSource
) : ConversationRepository {

    override fun observe(): Flow<List<Conversation>> {
        return local.observe()
    }

    override suspend fun refresh() {
        try {
            println("🌐 Buscando API...")
            delay(2000) // ⏳ simula latência de rede
            val remoteData = remote.getLastMessages()

            println("💾 Salvando no cache (${remoteData.size})")

            local.save(remoteData)

        } catch (e: Exception) {
            println("❌ Erro no refresh: ${e.message}")
            throw e // deixa ViewModel decidir
        }
    }
}