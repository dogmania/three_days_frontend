package com.example.threedays

import android.app.Application
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        KakaoSdk.init(this, "970d63044e9407925d6558936331bba2")
        FirebaseApp.initializeApp(this)
    }
}