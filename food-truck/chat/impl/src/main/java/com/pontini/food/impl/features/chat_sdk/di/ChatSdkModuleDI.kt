package com.pontini.food.impl.features.chat_sdk.di

import com.pontini.food.domain.model.Message
import com.pontini.food.impl.features.chat_sdk.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.features.chat_sdk.data.datasource.impl.ChatRemoteDataSourceImpl
import com.pontini.food.impl.features.chat_sdk.data.mappers.IncomingMessageMapper
import com.pontini.food.impl.features.chat_sdk.data.repositories.ChatRepositoryImpl
import com.pontini.food.impl.features.chat_sdk.domain.repositories.ChatRepository
import com.pontini.food.mapper.Mapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val INCOMING_MESSAGE = named("INCOMING_MESSAGE")

val chatSdkModule = module {
    single<ChatRepository> {
        ChatRepositoryImpl(
            remoteDataSource = get()
        )
    }

    factory<ChatRemoteDataSource> {
        ChatRemoteDataSourceImpl(
            client = get(),
            incomingMessageMapper = get(INCOMING_MESSAGE)
        )
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(WebSockets)
        }
    }

    factory<Mapper<String, Message>>(INCOMING_MESSAGE) {
        IncomingMessageMapper()
    }
}