package com.pontini.food.impl.core.mapper.di

import com.pontini.food.impl.core.mapper.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.core.mapper.data.datasource.impl.ConversationRemoteRemoteDataSourceImpl
import com.pontini.food.impl.core.mapper.data.repositories.ConversationRepositoryImpl
import com.pontini.food.impl.core.mapper.domain.repositories.ConversationRepository
import org.koin.dsl.module

val conversationsModuleData = module {
    single<ConversationRepository> {
        ConversationRepositoryImpl(
            remote = get(),
            local = get()
        )
    }

    factory<ConversationRemoteDataSource> {
        ConversationRemoteRemoteDataSourceImpl(
            get()
        )
    }
}