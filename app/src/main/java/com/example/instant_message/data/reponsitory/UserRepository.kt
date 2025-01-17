package com.example.instant_message.data.reponsitory

import android.util.Log
import com.example.instant_message.domain.entity.LoginRequest
import com.example.instant_message.domain.entity.LoginResponse
import com.example.instant_message.domain.entity.RegisterRequest
import com.example.instant_message.domain.entity.RegisterResponse
import com.example.instant_message.domain.manager.SessionManager
import com.example.instant_message.domain.network.UserService

class UserRepository(
    private val userService: UserService,
    private val sessionManager: SessionManager
) {
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = userService.login(LoginRequest(username, password))
            if(response.isSuccessful){
                Log.d("UserRepository", "Login request successful")
                response.body()?.let {
                   //存储token和username
                    it.token?.let { token ->
                        sessionManager.saveToken(token)
                    }
                    it.user?.username?.let { username ->
                        sessionManager.setUsername(username)
                        Log.d("UserRepository", "Username saved in sessionManager: $username")
                    }
                    it.user?.uuid?.let{ uuid ->
                        sessionManager.saveUUID(uuid.toString())
                        Log.d("UserRepository", "UUID saved in sessionManager: $uuid")
                    }
                    Result.success(it)
                } ?: Result.failure(Exception("Login failed"))
            }else{
                Log.e("UserRepository", "Login request failed with code: ${response.code()}")
                Result.failure(Exception("Login failed"))
            }
        }catch (e: Exception){
            Log.e("UserRepository", "Login request failed with exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun register(username: String, password: String): Result<RegisterResponse>{
        Log.d("UserRepository", "Attempting to register with username: $username")
        return try {
            val response = userService.register(RegisterRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Register failed"))
            }else{
                Result.failure(Exception("Register failed"))
            }
        }catch (e: Exception){
            Log.e("UserRepository", "Register request failed with exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}
