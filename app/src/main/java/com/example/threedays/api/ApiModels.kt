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

data class Habit(
    var achievementCount: Int,
    var achievementRate: Float,
    var comboCount: Int,
    var duration: Int,
    var id: Int,
    var title: String,
    var visible: Boolean
)

data class CreateHabit(
    val duration: Int,
    val email: String,
    val title: String,
    val visible: Boolean
)

data class UserInfo(
    val email: String,
    val keywords: List<String>,
    val nickname: String
)
