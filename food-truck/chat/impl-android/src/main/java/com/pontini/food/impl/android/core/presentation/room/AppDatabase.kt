package com.pontini.food.impl.android.core.presentation.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pontini.food.impl.android.data.datasource.impl.local.room.MessageDao
import com.pontini.food.impl.android.data.model.room.MessageEntity
import com.pontini.food.impl.android.data.datasource.impl.local.room.ConversationDao
import com.pontini.food.impl.android.data.model.room.ConversationEntity

@Database(
    entities = [ConversationEntity::class, MessageEntity::class ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}