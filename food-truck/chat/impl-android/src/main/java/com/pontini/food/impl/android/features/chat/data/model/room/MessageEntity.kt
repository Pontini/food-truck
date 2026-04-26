package com.pontini.food.impl.android.features.chat.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val message: String,
    val senderName: String,
    val timestamp: Long
)