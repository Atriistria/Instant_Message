package com.example.instant_message.domain.manager


import android.util.Log
import com.example.instant_message.domain.network.MyWebSocketListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString.Companion.toByteString
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.TimeUnit

object WebSocketManager {

    private val webSocketClient: OkHttpClient = OkHttpClient().newBuilder()
        .retryOnConnectionFailure(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null
    private val messageQueue: Queue<String> = LinkedList()
    private var isConnected = false
    fun connect(url: String, listener: MyWebSocketListener, token: String?){
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .build()
        webSocketClient.newWebSocket(request, listener)
    }

    fun setWebSocket(webSocket: WebSocket){
        this.webSocket = webSocket
        isConnected = true
        while (!messageQueue.isEmpty()){
            val message = messageQueue.poll()
            message?.let { sendMessage(it) }
        }
    }

    fun getWebSocket():WebSocket? {
        return webSocket
    }

    fun sendMessage(message: String){
        if(isConnected) {
            Log.d("WebSocketManager", "sendMessage: $message")
            webSocket?.send(message)
        }else{
            Log.d("WebSocketManager", "未连接")
            messageQueue.add(message)
        }
    }

    fun sendMessage(byteString: ByteArray){
        webSocket?.send(byteString.toByteString(0, byteString.size))
    }


    fun close(){
        webSocket?.close(1000, null)
    }

    private fun sendMessageInternal(message: String) {
        webSocket?.let {
            val success = it.send(message)
            if (success) {
                Log.d("WebSocketManager", "消息发送成功: $message")
            } else {
                Log.e("WebSocketManager", "消息发送失败: $message")
            }
        } ?: run {
            Log.e("WebSocketManager", "WebSocket 未初始化，消息无法发送: $message")
        }
    }

}


