package com.example.instant_message.domain.network

import android.content.Context
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.example.instant_message.AppLifecycleTracker
import com.example.instant_message.domain.entity.MessageResponse
import com.example.instant_message.domain.manager.WebSocketManager
import com.example.instant_message.domain.util.NotificationUtil
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.greenrobot.eventbus.EventBus


class MyWebSocketListener(
    private val context: Context,
    private val listener: WebSocketEventListener
): WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response){
        WebSocketManager.setWebSocket(webSocket)
        Log.d("MyWebSocketListener", "onOpen: ${response.code}")
        listener.onOpen(webSocket)
    }
    override fun onMessage(webSocket: WebSocket, text: String){
        try {
            val json = JSONObject.parseObject(text)

            val type = json.getString("type")
            val sender = json.getString("sender")
            val recipient = json.getString("recipient")
            val content = json.getString("content")
            val timestamp = json.getLong("timestamp")
            Log.d("MyWebSocketListener", "onMessage: $json")
            if(!AppLifecycleTracker.isAppInForeground){
                if (sender != "client"){
                    NotificationUtil.showNotification(context, sender, content)
                }
            }

            when(type){
                "chat" -> {
                    EventBus.getDefault().post(MessageResponse(content, sender, type, recipient, timestamp))
                }
                "group_chat" -> {
                    EventBus.getDefault().post(MessageResponse(content, sender, type, recipient, timestamp))
                }
                "image" -> {
                    EventBus.getDefault().post(MessageResponse(content, sender, type, recipient, timestamp))
                }
                "system" -> {
                    EventBus.getDefault().post(MessageResponse(content, sender, type, recipient, timestamp))
                }
                else -> {
                    Log.w("MyWebSocketListener", "onMessage: unknown type")
                }

            }
        }catch (e: Exception){
            Log.d("MyWebSocketListener", "onMessage: $e")
        }
        listener.onMessage(text)
    }


    override fun onMessage(webSocket: WebSocket, bytes: ByteString){
        listener.onMessage(bytes)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String){
        listener.onClosed()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        listener.onFailure(t)
    }
}