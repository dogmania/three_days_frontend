package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.threedays.databinding.ActivityFirstPageBinding
import com.google.firebase.messaging.FirebaseMessaging

class FirstPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartKakao.setOnClickListener{
            val intent= Intent( this,KakaoLoginActivity::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM Token", token) // 토큰을 콘솔에 출력
                // 토큰을 백엔드에 전달하는 코드를 추가하세요
            } else {
                Log.e("FCM Token", "토큰 가져오기 실패", task.exception)
            }
        }

    }
}