package com.example.instant_message.domain.entity

import com.example.instant_message.R

data class ChatListItem(
    val chatId: Int,
    val chatName: String,
    val lastMessage: String,
    val lastMessageTime: Long,
    val unreadCount: Int ?= 0,
    val isClient: Boolean,
    val isGroupChat: Boolean,
    val avatarUrl: Int,
    val senderName: String? = null
)