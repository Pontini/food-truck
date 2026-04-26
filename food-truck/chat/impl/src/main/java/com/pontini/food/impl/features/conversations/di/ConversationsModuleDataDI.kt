package com.pontini.food.impl.features.conversations.di

import com.pontini.food.impl.data.datasource.ConversationRemoteDataSource
import com.pontini.food.impl.features.conversations.data.datasource.impl.ConversationRemoteRemoteDataSourceImpl
import com.pontini.food.impl.features.conversations.data.repositories.ConversationRepositoryImpl
import com.pontini.food.impl.features.conversations.domain.repoistories.ConversationRepository
import org.koin.dsl.module

val conversationsModuleData = module {
    single<ConversationRepository> {
        ConversationRepositoryImpl(
            remote = get(),
            local = get()
        )
    }

    factory<ConversationRemoteDataSource> {
        ConversationRemoteRemoteDataSourceImpl(get())
    }
}