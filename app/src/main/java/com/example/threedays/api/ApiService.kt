package com.example.threedays.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/api/token")
    suspend fun checkJwtToken(@Body request: TokenRequest): TokenResponse //jwt 토큰이 유효한지 검증

    @POST("/api/userInfo")
    suspend fun saveProfile(@Body request: UserInfo) //닉네임과 키워드를 저장

    @POST("/api/habit")
    suspend fun createHabit(@Body request: CreateHabit) //새로운 습관 생성

    @GET("/api/habits")
    suspend fun getHabits(@Query("email") email: String) : MutableList<Habit> //습관 리스트를 가져옴

    @POST("/api/login")
    suspend fun getJwtToken(@Body kakaoAuthToken: String): TokenResponse //인가코드를 전달했을 때 jwt 토큰 반
}