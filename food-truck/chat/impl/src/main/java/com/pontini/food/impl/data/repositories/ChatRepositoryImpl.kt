package com.pontini.food.impl.data.repositories

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.domain.repoistories.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan
import com.pontini.food.impl.domain.model.ConnectionState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold

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
            .filterIsInstance<ConnectionState.Data>()
            .mapNotNull { event ->
                when (event) {
                    is ConnectionState.Data.MessageReceived -> event.message
                    is ConnectionState.Data.MessageSent -> event.message
                    else -> null
                }
            }

            // 👇 log individual
            .onEach {
                println("📩 [Repository] Mensagem: ${it.text} (${it.sender})")
            }

            // 👇 acumula corretamente sem reset
            .runningFold(emptyList<Message>()) { acc, message ->
                val updated = acc + message

                println("🧠 [Repository] Lista atualizada (${updated.size} msgs)")
                println("➡️ Última: ${message.text}")

                updated
            }

            // 👇 evita recomposição desnecessária
            .distinctUntilChanged()
    }

    override fun observeConnection(): Flow<ConnectionState> {
        return remoteDataSource.events
            .filterIsInstance<ConnectionState.Connection>()
    }
}
