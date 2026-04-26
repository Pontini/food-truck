package com.pontini.food.impl.android.core.presentation.room

import androidx.room.Room
import com.pontini.food.impl.android.core.presentation.mapper.Mapper
import com.pontini.food.impl.android.features.conversations.data.datasource.impl.local.ConversationLocalDataSourceImpl
import com.pontini.food.impl.android.features.conversations.data.datasource.mappers.ConversationDomainToEntityMapper
import com.pontini.food.impl.android.features.conversations.data.datasource.mappers.ConversationEntityToDomainMapper
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationEntity
import com.pontini.food.impl.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.domain.model.Conversation
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ENTITY_TO_DOMAIN = named("ENTITY_TO_DOMAIN")
val DOMAIN_TO_ENTITY = named("DOMAIN_TO_ENTITY")

val externalModules = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "food-db"
        ).fallbackToDestructiveMigration() // REMOVER ISSO AQUI
            .build()
    }

    // 📦 DAO
    single {
        get<AppDatabase>().conversationDao()
    }

    // 💾 Local DataSource
    single<ConversationLocalDataSource> {
        ConversationLocalDataSourceImpl(
            dao = get(),
            entityToDomain = get(ENTITY_TO_DOMAIN),
            domainToEntity = get(DOMAIN_TO_ENTITY)
        )
    }
    single<Mapper<ConversationEntity, Conversation>>(ENTITY_TO_DOMAIN) {
        ConversationEntityToDomainMapper()
    }

    single<Mapper<Conversation, ConversationEntity>>(DOMAIN_TO_ENTITY) {
        ConversationDomainToEntityMapper()
    }

}