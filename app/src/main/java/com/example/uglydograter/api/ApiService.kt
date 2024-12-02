package com.example.uglydograter.api

import com.example.uglydograter.models.LoginRequest
import com.example.uglydograter.models.LoginResponse
import com.example.uglydograter.models.LogoutRequest
import com.example.uglydograter.models.RegisterRequest
import com.example.uglydograter.models.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("logout")
    fun logout(@Body request: LogoutRequest): Call<Unit>
}
