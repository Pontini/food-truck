package com.pontini.food.impl.features.chat_sdk.data.datasource.impl

import com.pontini.food.domain.model.ConnectionState
import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.features.chat_sdk.data.mappers.WebSocketDataToMessageMapper
import com.pontini.food.mapper.Mapper
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
) : ChatRemoteDataSource {
    private var lastConversationID: String = ""

    private val _events =
        MutableSharedFlow<ConnectionState>(extraBufferCapacity = 64, replay = 1, )

    override val events: Flow<ConnectionState> = _events

    private var session: DefaultClientWebSocketSession? = null
    private var isConnected = false

    override suspend fun connect() {
        if (isConnected) return
        isConnected = true

        println("🟡 [WS] Connecting...")

        _events.tryEmit(ConnectionState.Connection.Connecting)

        try {
            client.webSocket("wss://ws.postman-echo.com/raw") {

                session = this

                println("🟢 [WS] Connected")

                _events.tryEmit(ConnectionState.Connection.Connected)

                while (true) {
                    val frame = incoming.receive()
                    val text = (frame as? Frame.Text)?.readText() ?: continue

                    println("📩 [WS] Received raw: $text")

                    val message = webSocketDataToMessageMapper.map(text, conversationID = lastConversationID)

                    println("📦 [WS] Mapped message: ${message.text}")

                    _events.tryEmit(ConnectionState.Data.MessageReceived(message))
                }
            }
        } catch (e: Exception) {
            isConnected = false

            println("🔴 [WS] Error: ${e.message}")

            _events.tryEmit(
                ConnectionState.Connection.Error(e.message ?: "Erro")
            )
        }
    }

    override suspend fun send(message: String, conversationId: String) {
        println("session = $session")
        lastConversationID = conversationId
        val currentSession = requireSession() ?: return

        println("📤 [WS] Sending: $message")

        try {
            currentSession.send(Frame.Text(message))
        } catch (e: Exception) {
            e.printStackTrace()

            println("🔴 [WS] Send error")

            _events.tryEmit(
                ConnectionState.Connection.Error("Erro ao enviar")
            )
        }
    }

    private fun requireSession(): DefaultClientWebSocketSession? {
        val current = session
        if (current == null) {
            _events.tryEmit(
                ConnectionState.Connection.Error("Sem conexão ativa")
            )
        }
        return current
    }
}