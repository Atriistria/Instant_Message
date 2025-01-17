package com.example.instant_message.domain.entity


data class MessageRequest(
    val event: String,
    val sender: String,
    val recipient: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
)