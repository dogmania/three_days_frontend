package com.example.threedays.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @POST("/api/token")
    suspend fun checkJwtToken(@Body request: TokenRequest): TokenResponse //jwt 토큰이 유효한지 검증

    @POST("/api/userInfo")
    suspend fun saveProfile(@Body request: UserInfo) //닉네임과 키워드를 저장

    @POST("/api/habit")
    suspend fun createHabit(@Body request: CreateHabit) //새로운 습관 생성

    @GET("/api/habits")
    suspend fun getHabits(@Query("email") email: String) : MutableList<Habit> //습관 리스트를 가져옴

    @GET("/api/habits/edit-list")
    suspend fun getHabitEditList(@Query("email") email: String): MutableList<ModifyFragmentHabit>

    @POST("/api/login")
    suspend fun getJwtToken(@Body kakaoAuthToken: String): KaKaoTokenResponse //인가코드를 전달했을 때 jwt 토큰 반

    @DELETE("/api/habits/{habitId}")
    suspend fun deleteHabit(@Path("habitId") habitId: Long)

    @PUT("/api/habits/{habitId}/stop")
    suspend fun stopHabit(@Path("habitId") habitId: Long)

    @PUT("/api/habits/{habitId}/edit")
    suspend fun updateHabit(
        @Path("habitId") habitId: Long,
        @Body editHabit: EditHabit
    )

    @Multipart
    @POST("/api/habits/{habitId}/certify")
    suspend fun certifyHabit(
        @Path("habitId") habitId: Long,
        @Part("level") level: RequestBody,
        @Part("review") review: RequestBody,
        @Part images: List<MultipartBody.Part>?
    )

    @GET("/api/feed/profile/me")
    suspend fun getMyProfileFeed(@Query("email") email: String): ProfileFeedResponse

    @GET("/api/feed/users")
    suspend fun getUserFeed(@Query("email") email: String): List<UserCertifiedHabit>

    @GET("/api/search")
    suspend fun searchUser(@Query("habitTitle") habitTitle: String): List<SearchedUser>

    @GET("/api/feed/profile/{userId}")
    suspend fun getUserProfile(
        @Path("userId") userId: Long,
        @Query("email") email: String
    ): ProfileFeedResponse

    @POST("/api/users/{fromUserId}/follow/{toUserId}")
    suspend fun follow(@Path("fromUserId") id: Long, @Path("toUserId") userId: Long)

    @DELETE("/api/users/{fromUserId}/unfollow/{toUserId}")
    suspend fun unFollow(@Path("fromUserId") id: Long, @Path("toUserId") userId: Long)
}