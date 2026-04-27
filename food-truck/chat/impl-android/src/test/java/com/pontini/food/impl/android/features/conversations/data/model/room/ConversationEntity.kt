package com.pontini.food.impl.android.features.conversations.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: Long
)