package com.example.instant_message.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.instant_message.R
import com.example.instant_message.ui.fragment.ChatFragment

class ChatActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_chat_container, ChatFragment())
                .commit()
        }
    }
}