package com.example.threedays.api

data class TokenRequest(
    val accessToken: String,
    val refreshToken: String
    )

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
    )

data class UserData(
    val email: String,
    val nickname: String
)