package com.example.instant_message.domain.network

import com.example.instant_message.domain.entity.LoginRequest
import com.example.instant_message.domain.entity.LoginResponse
import com.example.instant_message.domain.entity.RegisterRequest
import com.example.instant_message.domain.entity.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/api/user/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}