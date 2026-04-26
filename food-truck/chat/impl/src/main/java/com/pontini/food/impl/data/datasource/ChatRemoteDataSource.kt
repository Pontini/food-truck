package com.pontini.food.impl.data.datasource

import com.pontini.food.impl.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface ChatRemoteDataSource {

    suspend fun connect()

    suspend fun send(message: String)

    val events: Flow<ConnectionState>
}