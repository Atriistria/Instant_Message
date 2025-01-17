package com.example.instant_message

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.room.Room
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.service.WebSocketService
import com.example.instant_message.ui.activity.ChatActivity
import com.example.instant_message.ui.activity.LoginActivity

class MyApplication : Application(){
    private lateinit var sessionManager : SessionManager
    private lateinit var database: AppDatabase
    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        sessionManager = SessionManager.getInstance(this)
        sessionManager.isLoggedIn().observeForever { isLoggedIn ->
            if (isLoggedIn) {
                // 启动 WebSocketService
                val intent = Intent(this, WebSocketService::class.java)
                startService(intent)
            } else {
                Log.d("MyApplication", "isLoggedIn is false")
                // 停止 WebSocketService
                val intent = Intent(this, WebSocketService::class.java)
                stopService(intent)
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        sessionManager.isLoggedIn().removeObserver{ }
    }
}