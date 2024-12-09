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
    val id: String,
    val name: String,
    val birthdate: String,
    val rating: String,
    val url: String,
    val votes: String
)

data class PetsResponse(
    val success: Int,
    val pets: List<Pet>?,
    val errormessage: String?
)

data class AddPetResponse(
    val success: Int,
    val errormessage: String?
)

data class AddPetRequest(
    val token: String,
    val image: String,
    val name: String,
    val description: String,
    val birthday: String
)

data class ListRequest(
    val token: String
)

data class DeleteRequest(
    val token: String,
    val id: String
)

data class DeleteResponse(
    val success: Int,
    val errormessage: String?
)