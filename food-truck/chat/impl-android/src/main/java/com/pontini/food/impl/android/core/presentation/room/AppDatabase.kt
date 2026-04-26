package com.pontini.food.impl.android.core.presentation.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pontini.food.impl.android.features.chat.data.datasource.impl.local.room.MessageDao
import com.pontini.food.impl.android.features.chat.data.model.room.MessageEntity
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationDao
import com.pontini.food.impl.android.features.conversations.data.model.room.ConversationEntity

@Database(
    entities = [ConversationEntity::class, MessageEntity::class ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}