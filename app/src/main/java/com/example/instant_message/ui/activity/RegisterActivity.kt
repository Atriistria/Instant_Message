package com.example.instant_message.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.instant_message.data.factory.RegisterViewModelFactory
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.network.ApiClient
import com.example.instant_message.data.reponsitory.UserRepository
import com.example.instant_message.data.viewModel.RegisterViewModel
import com.example.instant_message.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val sessionManager = SessionManager.getInstance(this)
    private val viewModel: RegisterViewModel by viewModels {
       val userRepository = UserRepository(ApiClient.getUserService(), sessionManager)
        RegisterViewModelFactory(userRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.registerResult.observe(this){ result ->
            if(result.success){
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("username", result.user?.username)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.register.setOnClickListener{
            val username = binding.usernameInputRegister.text.toString()
            val password = binding.passwordInputRegister.text.toString()

            if(username.isNotEmpty() && password.isNotEmpty()){
                viewModel.register(username, password)
            }else{
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}