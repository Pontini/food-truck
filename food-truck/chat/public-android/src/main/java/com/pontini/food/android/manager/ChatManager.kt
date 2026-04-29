package com.pontini.food.android.manager

import com.pontini.food.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow
import com.pontini.food.domain.model.Message

interface ChatManager{
    suspend fun sendMessage(message: String,conversationId:String)
    fun getMessagesById(conversationId: String): Flow<List<Message>>
    fun getConnection(): Flow<ConnectionState>
}