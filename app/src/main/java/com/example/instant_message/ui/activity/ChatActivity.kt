package com.example.instant_message.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.instant_message.R
import com.example.instant_message.ui.fragment.ChatFragment

class ChatActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatName = intent.getStringExtra("chatName")
        Log.d("ChatActivity", "onCreate: $chatName")
        val chatFragment = ChatFragment()
        val bundle = Bundle().apply {
            putString("chatName", chatName)  // 传递参数
        }
        chatFragment.arguments = bundle
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_chat_container, chatFragment)
                .commit()
        }
    }
}