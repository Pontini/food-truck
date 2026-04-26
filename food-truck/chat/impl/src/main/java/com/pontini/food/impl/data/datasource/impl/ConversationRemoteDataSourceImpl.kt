package com.pontini.food.impl.data.datasource.impl

import com.pontini.food.impl.data.datasource.ConversationDataSource
import com.pontini.food.impl.data.model.ConversationResponseData
import com.pontini.food.impl.domain.model.Conversation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val LAST_CONVERSATIONS_ENDPOINT = "https://jsonplaceholder.typicode.com/users"

class ConversationRemoteDataSourceImpl(
    private val client: HttpClient
) : ConversationDataSource {

    override suspend fun getLastMessages(): List<Conversation> {
        val response: List<ConversationResponseData> =
            client.get(LAST_CONVERSATIONS_ENDPOINT).body()
        println(response+"AQQQQ")
        return response.map {
            Conversation(
                id = it.id.toString(),
                name = it.name,
                lastMessage = randomMessage(),
                timestamp = System.currentTimeMillis()
            )
        }
    }
}

private fun randomMessage(): String {
    return listOf(
        "Olá! Como posso ajudar?",
        "Seu pedido saiu para entrega",
        "Estamos verificando isso pra você",
        "Pode me passar mais detalhes?",
        "Obrigado pelo contato!",
        "Já resolvemos aqui",
        "Seu pedido foi confirmado!",
        "Em instantes te retorno"
    ).random()
}