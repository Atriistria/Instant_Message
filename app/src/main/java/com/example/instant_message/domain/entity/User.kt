package com.example.instant_message.domain.entity

import java.util.UUID

data class User(
    val uuid: UUID,
    val username: String,
    val createdAt: String,
    val updatedAt: String
)