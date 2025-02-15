package com.example.instant_message.data.reponsitory

import android.util.Log
import com.alibaba.fastjson.JSON
import com.example.instant_message.AppDatabase
import com.example.instant_message.domain.entity.ChatItem
import com.example.instant_message.domain.entity.MessageEntity
import com.example.instant_message.domain.entity.MessageRequest
import com.example.instant_message.domain.entity.MessageResponse
import com.example.instant_message.domain.util.WebsocketEvent.SEND_FRIEND_IMAGE
import com.example.instant_message.domain.util.WebsocketEvent.SEND_FRIEND_MESSAGE
import com.example.instant_message.domain.util.WebsocketEvent.SEND_GROUP_IMAGE
import com.example.instant_message.domain.util.WebsocketEvent.SEND_GROUP_MESSAGE
import com.example.instant_message.domain.manager.WebSocketManager
import com.example.instant_message.domain.util.ImageChunker
import com.example.instant_message.domain.util.ImageCompressor
import kotlinx.coroutines.runBlocking

class ChatRepository(
    private val database: AppDatabase
) {
    fun sendMessage(message: MessageRequest){
        val jsonString = JSON.toJSONString(message)
        Log.d("ChatRepository", "sendMessage: $jsonString")
        when(message.event){
            SEND_FRIEND_MESSAGE -> {
                sendFriendMessage(jsonString)
                val chatItem = ChatItem(
                    id = System.currentTimeMillis(),
                    message = message.content,
                    sender = "self"
                )
                saveChatItemToDatabase(chatItem)
            }
            SEND_FRIEND_IMAGE -> {
                val imageChunks = ImageCompressor.compressImage(message.content)
                ImageChunker.chunkImage(imageChunks, 1024 * 1024)
                sendFriendImage(jsonString)
            }
            SEND_GROUP_MESSAGE -> {

            }
            SEND_GROUP_IMAGE ->{

            }
        }
    }

    private fun saveChatItemToDatabase(chatItem: ChatItem) {
        Log.d("ChatRepository", "saveChatItemToDatabase: $chatItem")
        val messageEntity = MessageEntity(
            id = chatItem.id,
            sender = chatItem.sender,
            recipient = "atride",
            type = "chat",
            content = chatItem.message,
            timestamp = System.currentTimeMillis()
        )
        runBlocking {
            saveMessage(messageEntity)
        }
    }

    private fun sendFriendMessage(jsonString: String) {
        Log.d("ChatRepository", "sendFriendMessage: $jsonString")
        WebSocketManager.sendMessage(jsonString)
    }

    private fun sendFriendImage(jsonString: String) {
        WebSocketManager.sendMessage(jsonString)
    }

    suspend fun saveMessage(message: MessageEntity) {
        Log.d("ChatRepository", "saveMessage: $message")
        database.chatDao().insertMessage(message)
    }

    suspend fun getChatHistory(): List<MessageEntity> {
        return database.chatDao().getAllMessages()
    }

    suspend fun getChatHistoryBySender(sender: String): List<MessageEntity> {
        return database.chatDao().getMessagesBySender(sender)
    }


}