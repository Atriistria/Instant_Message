package com.example.instant_message.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.instant_message.MainActivity
import com.example.instant_message.R
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.service.WebSocketService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartActivity : AppCompatActivity() {
    private val sessionManager = SessionManager.getInstance(this)
    private val serviceIntent by lazy { Intent(this, WebSocketService::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val mainScope = CoroutineScope(Dispatchers.Main)

        mainScope.launch {
            try {
                delay(2000)
                sessionManager.isLoggedIn().observe(this@StartActivity) { isLoggedIn ->
                    if (isLoggedIn) {
                        if (!isServiceRunning(WebSocketService::class.java)) {
                            startService(serviceIntent)
                        }
                        startActivity(Intent(this@StartActivity, MainActivity::class.java))
                        finish()
                    }else{
                        startActivity(Intent(this@StartActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }catch (e: InterruptedException){
                e.printStackTrace()
            }
        }.start()
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}