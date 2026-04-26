package com.pontini.food.impl.data.datasource.impl

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.domain.model.ConnectionState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.UUID

class ChatRemoteDataSourceImpl(
    private val client: HttpClient
) : ChatRemoteDataSource {

    private val _events =
        MutableSharedFlow<ConnectionState>(extraBufferCapacity = 64)

    override val events: Flow<ConnectionState> = _events

    private var session: DefaultClientWebSocketSession? = null
    private var isConnected = false

    override suspend fun connect() {
        if (isConnected) return
        isConnected = true

        _events.emit(ConnectionState.Connection.Connecting)

        try {
            client.webSocket("wss://ws.postman-echo.com/raw") {

                session = this
                _events.emit(ConnectionState.Connection.Connected)

                println("✅ WebSocket conectado e aguardando mensagens...")

                // 👇 loop infinito enquanto conexão ativa
                while (true) {
                    val frame = incoming.receive()

                    val text = (frame as? Frame.Text)?.readText() ?: continue

                    println("📩 Recebido: $text")

                    _events.emit(
                        ConnectionState.Data.MessageReceived(
                            Message(
                                id = UUID.randomUUID().toString(),
                                text = text,
                                sender = "Server"
                            )
                        )
                    )
                }
            }
        } catch (e: Exception) {
            isConnected = false
            println("❌ Conexão caiu: ${e.message}")
            _events.emit(ConnectionState.Connection.Error(e.message ?: "Erro"))
        }
    }

    override suspend fun send(message: String) {
        val currentSession = session

        if (currentSession == null) {
            println("❌ Tentou enviar sem conexão ativa")
            return
        }

        try {
            println("📤 Enviando: $message")

            currentSession.send(Frame.Text(message))

        } catch (e: Exception) {
            println("❌ Erro ao enviar: ${e.message}")
            e.printStackTrace()

            _events.emit(ConnectionState.Connection.Error("Erro ao enviar"))
        }

        _events.emit(
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