package com.example.threedays

import android.app.Application
import com.bumptech.glide.Glide
import com.example.threedays.api.ApiService
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GlobalApplication : Application() {
    lateinit var apiService: ApiService
    var nickname: String = ""
    var email: String = ""

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        val retrofit = Retrofit.Builder()
            .baseUrl("http://threedays-env-1.eba-di3ir3iz.ap-northeast-2.elasticbeanstalk.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        KakaoSdk.init(this, "970d63044e9407925d6558936331bba2")
        FirebaseApp.initializeApp(this)

    }
}