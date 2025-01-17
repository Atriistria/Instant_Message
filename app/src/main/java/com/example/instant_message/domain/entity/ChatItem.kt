package com.example.instant_message.domain.entity

import com.example.instant_message.R

data class ChatItem(
    val id: Long,
    val imageResId: Int? = R.drawable.ic_launcher_background,
    val message: String,
    val sender: String
)