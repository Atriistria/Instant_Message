package com.example.instant_message.domain.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.instant_message.domain.entity.MessageEntity

@Dao
interface ChatDao {

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM message ORDER BY timestamp DESC")
    suspend fun getAllMessages(): List<MessageEntity>

    @Query("SELECT * FROM message WHERE sender = :sender OR recipient = :sender ORDER BY timestamp ASC")
    suspend fun getMessagesBySender(sender: String): List<MessageEntity>

    @Query("DELETE FROM message")
    suspend fun deleteAllMessages()
}