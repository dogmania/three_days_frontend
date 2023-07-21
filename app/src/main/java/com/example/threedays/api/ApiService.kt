package com.example.threedays.api

import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/token")
    suspend fun getJwtToken(@Body request: TokenRequest): TokenResponse

    @POST("/api/login")
    suspend fun getUserData(@Body request: String) : UserData
}