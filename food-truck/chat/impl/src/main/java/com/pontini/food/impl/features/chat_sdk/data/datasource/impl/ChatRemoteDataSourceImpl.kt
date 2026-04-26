package com.pontini.food.impl.features.chat_sdk.data.datasource.impl

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.features.chat_sdk.domain.model.ConnectionState
import com.pontini.food.mapper.Mapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.UUID

class ChatRemoteDataSourceImpl(
    private val client: HttpClient,
    private val webSocketDataToMessageMapper: Mapper<String, Message>,
) : ChatRemoteDataSource {

    private val _events =
        MutableSharedFlow<ConnectionState>(extraBufferCapacity = 64)

    override val events: Flow<ConnectionState> = _events

    private var session: DefaultClientWebSocketSession? = null
    private var isConnected = false

    override suspend fun connect() {
        if (isConnected) return
        isConnected = true

        _events.tryEmit(ConnectionState.Connection.Connecting)

        try {
            client.webSocket("wss://ws.postman-echo.com/raw") {

                session = this
                _events.tryEmit(ConnectionState.Connection.Connected)

                println("✅ WebSocket conectado e aguardando mensagens...")

                while (true) {
                    val frame = incoming.receive()

                    val text = (frame as? Frame.Text)?.readText() ?: continue

                    println("📩 Recebido: $text")

                    val message = webSocketDataToMessageMapper.map(text)

                    val emitted = _events.tryEmit(
                        ConnectionState.Data.MessageReceived(message)
                    )

                    if (!emitted) {
                        println("⚠️ Evento descartado (buffer cheio)")
                    }
                }
            }
        } catch (e: Exception) {
            isConnected = false
            println("❌ Conexão caiu: ${e.message}")

            _events.tryEmit(
                ConnectionState.Connection.Error(e.message ?: "Erro")
            )
        }
    }

    override suspend fun send(message: String) {
        val currentSession = session

        if (currentSession == null) {
            println("❌ Tentou enviar sem conexão ativa")
            return
        }

        try {
            currentSession.send(Frame.Text(message))

        } catch (e: Exception) {
            e.printStackTrace()

            _events.tryEmit(
                ConnectionState.Connection.Error("Erro ao enviar")
            )
        }

        _events.tryEmit(
            ConnectionState.Data.MessageSent(
                Message(
                    id = UUID.randomUUID().toString(),
                    text = message,
                    sender = "Me"
                )
            )
        )
    }
}