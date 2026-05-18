package com.pontini.food.impl.data.repositories

import com.pontini.food.domain.models.ChatEvent
import com.pontini.food.domain.repositories.ChatRepository
import com.pontini.food.domain.models.ConnectionStatus
import com.pontini.food.features.domain.models.Message
import com.pontini.food.features.domain.models.TypeMessage
import com.pontini.food.impl.core.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.data.model.request.SendMessageRequest
import com.pontini.food.impl.data.datasource.ChatLocalDataSource
import com.pontini.food.impl.core.mapper.domain.model.excpetion.FailedSaveMessageException
import com.pontini.food.impl.core.mapper.domain.model.excpetion.SendMessageException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge

class ChatRepositoryImpl(
    private val local: ChatLocalDataSource,
    private val remote: ChatRemoteDataSource,
) : ChatRepository {

    override suspend fun connect() {
        remote.connect()
    }

    override suspend fun sendMessage(message: String, conversationId: String) {
        try {
            val sendMessageRequest = SendMessageRequest(
                conversationId = conversationId,
                message = message,
                isSent = true
            )
            remote.send(message, conversationId)
            local.insert(sendMessageRequest)
        } catch (e: SendMessageException) {
            e.printStackTrace()
            // Aqui a gente pode tratar o erro/ fazer alguma coisa especifica mas sem conhecer os detalhes da conexão
        }
        catch (e: FailedSaveMessageException) {
            e.printStackTrace()
            // Aqui a gente pode tratar o erro/ fazer alguma coisa especifica  mas sem conhecer os detalhes do room
        }

    }

    override fun getMessagesById(conversationId: String): Flow<List<Message>> {
        val localFlow = local.getMessages(conversationId)

        val remoteSyncFlow = flow<Nothing> {
            remote.chatEvent.filterIsInstance<ChatEvent.MessageReceived>()
                .collect { data ->
                    local.insert(
                        SendMessageRequest(
                            conversationId = conversationId,
                            message = data.message.text,
                            isSent = data.message.typeMessage == TypeMessage.SENT
                        )
                    )
                }
        }

        return merge(localFlow, remoteSyncFlow)
    }

    override fun getConnection(): Flow<ConnectionStatus> {
        return remote.connectionStatus
    }
}