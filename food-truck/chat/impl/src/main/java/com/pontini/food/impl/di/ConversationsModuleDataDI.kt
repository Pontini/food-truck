package com.pontini.food.impl.di

import com.pontini.food.domain.repositories.ConversationRepository
import com.pontini.food.impl.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.data.datasource.impl.ConversationRemoteRemoteDataSourceImpl
import com.pontini.food.impl.data.repositories.ConversationRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val conversationsModuleData = module {

    single<CoroutineScope> { AppScope() }


    single<ConversationRepository> {
        ConversationRepositoryImpl(
            remote = get(),
            local = get(),
            ioDispatcher = Dispatchers.IO,
            scope = get(),
        )
    }

    factory<ConversationRemoteDataSource> {
        ConversationRemoteRemoteDataSourceImpl(get())
    }
}

class AppScope : CoroutineScope {
    override val coroutineContext =
        SupervisorJob() + Dispatchers.Default
}