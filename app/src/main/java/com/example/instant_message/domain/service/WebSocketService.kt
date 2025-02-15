package com.example.instant_message.domain.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.instant_message.AppLifecycleTracker
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.manager.WebSocketManager
import com.example.instant_message.domain.network.WebSocketEventListener
import com.example.instant_message.domain.network.MyWebSocketListener
import com.example.instant_message.domain.util.NotificationUtil
import okhttp3.WebSocket
import okio.ByteString

class WebSocketService: Service() {

    private lateinit var sessionManager: SessionManager
    override fun onCreate() {
        super.onCreate()
        sessionManager = SessionManager.getInstance(this)
        val token = sessionManager.getToken()
        val url = "ws://10.0.2.2:8080/ws/chat"
        val listener = MyWebSocketListener(this,object : WebSocketEventListener {
            override fun onOpen(webSocket: WebSocket) {
                Log.d("WebSocketService", "WebSocket已连接")
            }

            override fun onMessage(message: String) {
                Log.d("WebSocketService", "收到消息: $message")

            }

            override fun onMessage(byteString: ByteString) {

            }

            override fun onClosed() {
                Log.d("WebSocketService", "WebSocket连接已关闭")
            }

            override fun onFailure(error: Throwable) {
                Log.e("WebSocketService", "WebSocket连接失败: ${error.message}")
                stopSelf()
            }

        })

       WebSocketManager.connect(url, listener,token)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}