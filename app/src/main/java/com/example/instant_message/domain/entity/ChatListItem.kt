package com.example.instant_message.domain.entity

import com.example.instant_message.R

data class ChatListItem(
    val chatId: Int,            // 聊天记录的唯一标识
    val chatName: String,       // 聊天对象名称（好友名或群组名）
    val lastMessage: String,    // 最后一条消息
    val lastMessageTime: Long,  // 最后一条消息的时间戳
    val unreadCount: Int ?= 0,       // 未读消息数
    val isGroupChat: Boolean,   // 是否是群聊
    val avatarUrl: Int,      // 聊天对象的头像URL
    val senderName: String? = null // 如果需要，发送者的名称（用于群聊场景）
)