package com.pontini.food.impl.android.features.conversations.data.datasource.impl.local

import com.pontini.food.mapper.Mapper
import com.pontini.food.mapper.mapList
import com.pontini.food.impl.android.features.conversations.data.datasource.impl.local.room.ConversationDao
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationEntity
import com.pontini.food.impl.data.datasource.ConversationLocalDataSource
import com.pontini.food.impl.domain.model.Conversation
import com.pontini.food.impl.domain.model.excpetion.FailedGetMessages
import com.pontini.food.impl.core.domain.model.excpetion.FailedSaveConversationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConversationLocalDataSourceImpl(
    private val dao: ConversationDao,
    private val entityToDomain: Mapper<ConversationEntity, Conversation>,
    private val domainToEntity: Mapper<Conversation, ConversationEntity>
) : ConversationLocalDataSource {

    override fun getMessages(): Flow<List<Conversation>> {
        try {
            return dao.observe().map { list ->
                entityToDomain.mapList(list)
            }
        } catch (e: Exception) {
            throw FailedGetMessages()
        }
    }

    override suspend fun save(list: List<Conversation>) {
        try {
            dao.insertAll(domainToEntity.mapList(list))
        } catch (e: Exception) {
            throw FailedSaveConversationException()
        }
    }
}