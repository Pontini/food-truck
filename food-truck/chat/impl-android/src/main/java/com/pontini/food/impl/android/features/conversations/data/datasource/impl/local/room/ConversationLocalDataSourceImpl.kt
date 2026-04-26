package com.pontini.food.impl.android.features.conversations.data.datasource.impl.local.room

import com.pontini.food.mapper.Mapper
import com.pontini.food.mapper.mapList
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationDao
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationEntity
import com.pontini.food.impl.features.conversations.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.features.conversations.domain.model.Conversation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConversationLocalDataSourceImpl(
    private val dao: ConversationDao,
    private val entityToDomain: Mapper<ConversationEntity, Conversation>,
    private val domainToEntity: Mapper<Conversation, ConversationEntity>
) : ConversationLocalDataSource {

    override fun observe(): Flow<List<Conversation>> {
        return dao.observe().map { list ->
            entityToDomain.mapList(list)
        }
    }

    override suspend fun save(list: List<Conversation>) {
        dao.insertAll(domainToEntity.mapList(list))
    }
}