package com.pontini.food.impl.data.repositories

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.domain.repoistories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan
import com.pontini.food.impl.domain.model.ConnectionState
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource
) : ChatRepository {

    override suspend fun connect() {
        remoteDataSource.connect()
    }

    override suspend fun sendMessage(message: String) {
        remoteDataSource.send(message)
    }

    override fun observeMessages(): Flow<List<Message>> {
        return remoteDataSource.events
            .filterIsInstance<ConnectionState.Data.MessageReceived>()
            .onEach {
                println("📩 [Repository] Recebido: ${it.message.text}")
            }
            .map { it.message }
            .scan(emptyList()) { acc, message ->
                val updated = acc + message
                println("🧠 [Repository] Lista atualizada (${updated.size} msgs)")
                println("➡️ Última: ${message.text}")
                updated
            }
    }

    override fun observeConnection(): Flow<ConnectionState> {
        return remoteDataSource.events
            .filterIsInstance<ConnectionState.Connection>()
    }
}
