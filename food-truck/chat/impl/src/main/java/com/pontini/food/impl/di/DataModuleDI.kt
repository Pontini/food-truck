package com.pontini.food.impl.di

import com.pontini.food.impl.data.datasource.ChatRemoteDataSource
import com.pontini.food.impl.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.data.datasource.impl.ChatRemoteDataSourceImpl
import com.pontini.food.impl.data.datasource.impl.ConversationRemoteRemoteDataSourceImpl
import com.pontini.food.impl.data.repositories.ChatRepositoryImpl
import com.pontini.food.impl.data.repositories.ConversationRepositoryImpl
import com.pontini.food.impl.domain.repoistories.ChatRepository
import com.pontini.food.impl.domain.repoistories.ConversationRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModules = module {

    single<ChatRepository> {
        ChatRepositoryImpl(
            remoteDataSource = get()
        )
    }

    single<ConversationRepository> {
        ConversationRepositoryImpl(
            remote = get(),
            local = get()
        )
    }

    factory<ChatRemoteDataSource> {
        ChatRemoteDataSourceImpl(get())
    }


    factory<ConversationRemoteDataSource> {
        ConversationRemoteRemoteDataSourceImpl(get())
    }
    // evita crash com campos extras da API. Como eu estou usando ela pra simular as conversas,
    // tem campos que não são usados no app, então eu coloquei esse ignoreUnknownKeys pra evitar erro por causa disso e a gente pode seguir.
    // PORÉM, quero ressaltar que em um cenário real, onde a API é controlada por nós,
    // é melhor evitar enviar campos extras ou ajustar o modelo de dados para refletir exatamente o que a API retorna, para garantir uma melhor manutenção e clareza do código.
    // Nessa situacao a gente nao pode crashar o app para usuario mas é importante ter tratamento de erro inesperado e usar observabilidade para monitorar.
    // Imagina a gente tratar um dado importante como saldo da carteira, só pq nao fez o parse correto, mostrar valor default zero ? Usuario com saldo, mas app mostrando zero, isso pode gerar confusão e insatisfação.


    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(WebSockets)
        }
    }
}