package com.example.instant_message.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String,
    val recipient: String,
    val type: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)