package com.pontini.food.impl.features.chat_sdk.data.datasource.impl

import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.features.chat_sdk.data.mappers.WebSocketDataToMessageMapper
import com.pontini.food.impl.features.chat_sdk.domain.model.SendMessageException
import com.pontini.food.observability.ObservabilityFacade
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.atomic.AtomicBoolean

class ChatRemoteDataSourceImpl(
    private val client: HttpClient,
    private val webSocketDataToMessageMapper: WebSocketDataToMessageMapper,
    private val observabilityFacade: ObservabilityFacade
) : ChatRemoteDataSource {

    private var lastConversationID: String = ""

    private val _connectionState =
        MutableStateFlow<ConnectionState.Connection>(ConnectionState.Connection.Init)

    override val connectionState: Flow<ConnectionState.Connection> = _connectionState

    private val _messages = MutableSharedFlow<Message>(
        extraBufferCapacity = 64
    )

    override val messages: Flow<Message> = _messages

    private var session: DefaultClientWebSocketSession? = null

    private val isConnected = AtomicBoolean(false)

    override suspend fun connect() {
        if (!isConnected.compareAndSet(false, true)) {
            observabilityFacade.log("ws_already_connected")
            return
        }

        observabilityFacade.log("ws_connecting")

        _connectionState.value = ConnectionState.Connection.Connecting

        try {
            client.webSocket("wss://ws.postman-echo.com/raw") {

                session = this

                observabilityFacade.log("ws_connected")

                observabilityFacade.metric("ws_connection_success", 1.0)

                _connectionState.value = ConnectionState.Connection.Connected

                for (frame in incoming) {
                    val text = (frame as? Frame.Text)?.readText() ?: continue

                    observabilityFacade.log("ws_message_received")

                    val message = webSocketDataToMessageMapper.map(
                        text,
                        conversationID = lastConversationID
                    )

                    _messages.tryEmit(message)
                }
            }
        } catch (e: Exception) {
            isConnected.set(false)

            observabilityFacade.log("ws_connection_failed")

            observabilityFacade.metric("ws_connection_failed", 1.0)

            observabilityFacade.error(e)

            _connectionState.value =
                ConnectionState.Connection.FailedConnected(e.message ?: "Erro")
        }
    }

    override suspend fun send(message: String, conversationId: String) {
        lastConversationID = conversationId

        val currentSession = requireSession() ?: return

        observabilityFacade.log("ws_send_message")

        try {
            currentSession.send(Frame.Text(message))

            observabilityFacade.metric("ws_send_success", 1.0)

        } catch (e: Exception) {

            observabilityFacade.log("ws_send_failed")

            observabilityFacade.metric("ws_send_failed", 1.0)

            observabilityFacade.error(e)

            throw SendMessageException("Falha ao enviar mensagem. Tente novamente.")
        }
    }

    private fun requireSession(): DefaultClientWebSocketSession? {
        val current = session

        if (current == null) {
            observabilityFacade.log("ws_no_session")
            return null
        }

        return current
    }
}