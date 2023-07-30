package com.example.threedays.api

import com.google.android.datatransport.runtime.dagger.multibindings.IntoMap
import java.time.LocalDateTime

data class TokenRequest(
    val accessToken: String,
    val refreshToken: String
    )

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
    )

data class Habit(
    val achievementCount: Int,
    val achievementRate: Int,
    val comboCount: Int,
    val duration: Int,
    val id: Long,
    val title: String,
    val visible: Boolean,
    var isChecked: Boolean
)

data class ModifyFragmentHabit(
    val achievementCount: Int,
    val achievementRate: Int,
    val comboCount: Int,
    val duration: Int,
    val id: Long,
    val stopDate: String?,
    val title: String,
    val visible: Boolean,
    var isChecked: Boolean
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

data class KaKaoTokenResponse(
    val email: String,
    val nickname: String,
    val tokenDto: TokenResponse,
    val userId: Long
)

data class EditHabit(
    val title: String,
    val duration: Int,
    val visible: Boolean
)

data class CertifyData(
    val level: Int,
    val review: String
)

data class ProfileFeedResponse(
    val followerCount: Int,
    val habitList: List<CertifiedHabit>,
    val kakaoImageUrl: String,
    val keywords: List<String>,
    val nickname: String,
    val totalAchievementRate: Int,
    val totalHabitCount: Int,
    var isFollowing: Boolean
)

data class CertifiedHabit(
    val certifyDtos: List<Certification>,
    val createdHabit: String,
    val habitId: Long,
    val title: String
)

data class Certification(
    val certifiedDate: String,
    val certifyId: Long,
    val imagUrls: List<String>,
    val level: Int,
    val review: String
)

data class UserCertifiedHabit(
    val certifiedDate: String,
    val certifyImages: List<String>,
    val createdHabit: String,
    val kakaoImageUrl: String,
    val level: Int,
    val nickname: String,
    val review: String,
    val title: String,
    val userId: Long
)

data class SearchedUser(
    val kakaoProfileUrl: String,
    val nickname: String,
    val title: String,
    val userId: Long
)
