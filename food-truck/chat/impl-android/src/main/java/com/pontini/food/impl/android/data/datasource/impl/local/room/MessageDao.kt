package com.pontini.food.impl.android.data.datasource.impl.local.room


import androidx.room.*
import com.pontini.food.impl.android.data.model.room.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("""
        SELECT * FROM messages 
        WHERE conversationId = :conversationId 
        ORDER BY timestamp DESC
    """)
    fun observe(conversationId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<MessageEntity>)

    @Query("DELETE FROM messages")
    suspend fun clear()
}