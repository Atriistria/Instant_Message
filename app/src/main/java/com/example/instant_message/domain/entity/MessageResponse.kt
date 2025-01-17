package com.example.instant_message.domain.entity

data class MessageResponse(
    val content: String,
    val sender: String,
    val type: String,
    val recipient: String?,
    val timestamp: Long
)
