package com.pontini.food.android.manager

import kotlinx.coroutines.flow.Flow
import com.pontini.food.domain.model.Message

interface ChatManager{
    suspend fun sendMessage(message: String)
    fun observeMessages(): Flow<List<Message>>
}