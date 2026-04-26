package com.pontini.food.impl.di

import com.pontini.food.impl.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.data.datasource.impl.ChatRemoteDataSourceImpl
import com.pontini.food.impl.data.repositories.ChatRepositoryImpl
import com.pontini.food.impl.domain.repoistories.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import org.koin.dsl.module

val chatCoreModules = module {
    single<ChatRepository> {
        ChatRepositoryImpl(
            remoteDataSource = get()
        )
    }

    factory<ChatRemoteDataSource> {
        ChatRemoteDataSourceImpl(get())
    }

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(WebSockets)
        }
    }
}