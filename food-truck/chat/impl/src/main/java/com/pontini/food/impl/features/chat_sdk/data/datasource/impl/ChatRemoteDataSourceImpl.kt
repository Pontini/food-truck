package com.pontini.food.impl.features.chat_sdk.data.datasource.impl

import com.pontini.food.domain.model.ConnectionState
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

class ChatRemoteDataSourceImpl(
    private val client: HttpClient,
    private val webSocketDataToMessageMapper: WebSocketDataToMessageMapper,
    private val observabilityFacade: ObservabilityFacade
) : ChatRemoteDataSource {

    private var lastConversationID: String = ""

    private val _events =
        MutableSharedFlow<ConnectionState>(extraBufferCapacity = 64, replay = 1)

    override val events: Flow<ConnectionState> = _events

    private var session: DefaultClientWebSocketSession? = null
    private var isConnected = false

    override suspend fun connect() {
        if (isConnected) {
            observabilityFacade.log(
                "ws_already_connected",
                mapOf("conversationId" to lastConversationID)
            )
            return
        }

        isConnected = true

        observabilityFacade.log("ws_connect_start")

        _events.tryEmit(ConnectionState.Connection.Connecting)

        try {
            client.webSocket("wss://ws.postman-echo.com/raw") {

                session = this

                observabilityFacade.log("ws_connected_success")

                observabilityFacade.metric(
                    name = "ws_connection_success",
                    value = 1.0
                )

                _events.tryEmit(ConnectionState.Connection.Connected)

                while (true) {
                    val frame = incoming.receive()
                    val text = (frame as? Frame.Text)?.readText() ?: continue

                    observabilityFacade.log(
                        "ws_message_received_raw",
                        mapOf("payload_size" to text.length)
                    )

                    val message = webSocketDataToMessageMapper.map(
                        text,
                        conversationID = lastConversationID
                    )

                    observabilityFacade.log(
                        "ws_message_mapped",
                        mapOf("message_length" to message.text.length)
                    )

                    _events.tryEmit(ConnectionState.Data.MessageReceived(message))
                }
            }
        } catch (e: Exception) {
            isConnected = false

            observabilityFacade.log(
                "ws_connection_error",
                mapOf("error" to (e.message ?: "unknown"))
            )

            observabilityFacade.metric(
                name = "ws_connection_failed",
                value = 1.0
            )

            observabilityFacade.error(e)

            _events.tryEmit(
                ConnectionState.Connection.FailedConnected(e.message ?: "Erro")
            )
        }
    }

    override suspend fun send(message: String, conversationId: String) {
        lastConversationID = conversationId

        val currentSession = requireSession() ?: return

        observabilityFacade.log(
            "ws_send_message",
            mapOf(
                "conversationId" to conversationId,
                "message_length" to message.length
            )
        )

        try {
            currentSession.send(Frame.Text(message))

            observabilityFacade.metric(
                name = "ws_send_success",
                value = 1.0
            )

        } catch (e: Exception) {

            observabilityFacade.log(
                "ws_send_error",
                mapOf("error" to (e.message ?: "unknown"))
            )

            observabilityFacade.metric(
                name = "ws_send_failed",
                value = 1.0
            )

            observabilityFacade.error(e)

            throw SendMessageException("Falha ao enviar mensagem. Tente novamente.")
        }
    }

    private fun requireSession(): DefaultClientWebSocketSession? {
        val current = session

        if (current == null) {
            observabilityFacade.log("ws_no_active_session")
            return null
        }

        return current
    }
}