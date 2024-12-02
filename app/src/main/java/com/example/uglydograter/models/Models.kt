package com.example.uglydograter.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Int,
    val errormessage: String?,
    val token: String?
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val success: Int,
    val errormessage: String?
)

data class LogoutRequest(
    val token: String
)

data class Pet(
    val id: Int,
    val name: String,
    val breed: String,
    val age: Int
)

data class PetsResponse(
    val success: Int,
    val pets: List<Pet>?,
    val errormessage: String?
)
