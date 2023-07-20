package com.example.threedays.api

data class TokenRequest(
    val accessToken: String,
    val refreshToken: String
    )

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
    )