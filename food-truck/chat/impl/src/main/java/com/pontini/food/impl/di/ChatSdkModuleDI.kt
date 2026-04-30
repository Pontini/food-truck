package com.pontini.food.impl.di

import com.pontini.food.impl.core.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.data.mappers.WebSocketDataToMessageMapper
import com.pontini.food.impl.data.repositories.ChatRepositoryImpl
import com.pontini.food.impl.data.datasource.impl.ChatRemoteDataSourceImpl
import com.pontini.food.impl.domain.repositories.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val chatSdkModule = module {
    single<ChatRepository> {
        ChatRepositoryImpl(
            remote = get(),
            local = get()
        )
    }

    factory<ChatRemoteDataSource> {
        ChatRemoteDataSourceImpl(
            client = get(),
            webSocketDataToMessageMapper = WebSocketDataToMessageMapper(),
            observabilityFacade = get()
        )
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true // Somente paliativo pois a API é de teste e pode retornar campos extras que não estão mapeados no modelo de dados
                })
            }
            install(WebSockets)
        }
    }
}