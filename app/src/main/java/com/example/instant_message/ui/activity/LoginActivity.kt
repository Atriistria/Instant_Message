package com.example.instant_message.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.instant_message.MainActivity
import com.example.instant_message.domain.network.ApiClient
import com.example.instant_message.data.factory.LoginViewModelFactory
import com.example.instant_message.data.reponsitory.UserRepository
import com.example.instant_message.data.viewModel.LoginViewModel
import com.example.instant_message.databinding.ActivityLoginBinding
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.service.WebSocketService

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val sessionManager = SessionManager.getInstance(this)

    private val viewModel: LoginViewModel by viewModels {
        val userService = ApiClient.getUserService()
        val repository = UserRepository(userService,sessionManager)
        LoginViewModelFactory(repository)
    }

    private val serviceIntent by lazy { Intent(this, WebSocketService::class.java) }

    override fun onStart() {
        super.onStart()
        sessionManager.isLoggedIn().observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                if (!isServiceRunning(WebSocketService::class.java)) {
                    startService(serviceIntent)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       binding = ActivityLoginBinding.inflate(layoutInflater)
       setContentView(binding.root)

       viewModel.loginResult.observe(this) { result ->
           if (result.success) {
               if (!isServiceRunning(WebSocketService::class.java)) {
                   startService(serviceIntent)
               }
               Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
               sessionManager.setLoggedIn(true)
               startActivity(Intent(this, ChatActivity::class.java))
           } else {
               // 登录失败，显示错误信息
               Toast.makeText(this, "登录失败了: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
           }
       }
       binding.login.setOnClickListener{
           val username = binding.usernameInputLogin.text.toString()
           val password = binding.passwordInputLogin.text.toString()

           if (username.isNotEmpty() && password.isNotEmpty()){
               viewModel.login(username, password)
           } else {
               Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
           }
       }

       binding.registerButton.setOnClickListener{
           startActivity(Intent(this, RegisterActivity::class.java))
       }
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

    override fun onResume() {
        super.onResume()
        val username = intent.getStringExtra("username")
        Log.d("test", "onResume: $username")
        viewModel.setUsername(username)
        viewModel.username.observe(this){
            binding.usernameInputLogin.setText(username)
        }
    }
}