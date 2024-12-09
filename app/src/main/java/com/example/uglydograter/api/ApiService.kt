package com.example.uglydograter.api

import com.example.uglydograter.models.AddPetRequest
import com.example.uglydograter.models.AddPetResponse
import com.example.uglydograter.models.DeleteRequest
import com.example.uglydograter.models.DeleteResponse
import com.example.uglydograter.models.ListRequest
import com.example.uglydograter.models.LoginRequest
import com.example.uglydograter.models.LoginResponse
import com.example.uglydograter.models.LogoutRequest
import com.example.uglydograter.models.PetsResponse
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

    @POST("pets/list")
    fun listPets(@Body request: ListRequest): Call<PetsResponse>

    @POST("pets/add")
    fun addPet(@Body request: AddPetRequest): Call<AddPetResponse>

    @POST("pets/delete")
    fun deletePet(@Body request: DeleteRequest): Call<DeleteResponse>
}
