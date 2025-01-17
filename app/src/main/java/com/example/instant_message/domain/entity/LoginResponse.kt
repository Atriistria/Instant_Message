package com.example.instant_message.domain.entity

data class LoginResponse(
    val user: User? = null,
    val token: String? = null,
    val error: String? = null
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterResponse(
    val user: User? = null,
    val error: String? = null,
    val message: String? = null
)

data class LoginResult(
    val success: Boolean,
    val user: User? = null,
    val errorMessage: String? = null,
)

data class RegisterResult(
    val success: Boolean,
    val user: User? = null,
    val errorMessage: String? = null
)

