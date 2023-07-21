package com.example.threedays

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import com.example.threedays.api.ApiService
import com.example.threedays.api.TokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val app = applicationContext as GlobalApplication
        //applicationContext 변수를 사용하면 Application 클래스를 상속받은 클래스의 변수에 접근하여 앱 전체에서 사용되는 데이터에 접근 가능하다.
        //이를 통해 앱 전체에서 사용되는 데이터를 관리하거나 리소스에 접근하는 것이 가능하다.

        onAppStart(this)

        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({
            val accessToken = getAccessToken()
            val refreshToken = getRefreshToken()

            if (accessToken == null || refreshToken == null) {
                val intent= Intent( this,FirstPageActivity::class.java)
                startActivity(intent)
            } else {
                CoroutineScope(Dispatchers.IO).launch{
                    try {
                        val tokenResponse = app.apiService.getJwtToken(TokenRequest(accessToken, refreshToken))

                        saveAccessToken(tokenResponse.accessToken)
                        saveRefreshToken(tokenResponse.refreshToken)

                        getUserData(tokenResponse.accessToken)

                    } catch (e: Exception) {
                        Log.e("SplashActivity", "Error during API call: ${e.message}", e)
                    }
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            // 이전 키를 눌렀을 때 스플래시 스크린 화면으로 이동을 방지하기 위해
            // 이동한 다음 사용안함으로 finish 처리
            finish()

        }, 2000) // 시간 2초 이후 실행
    }

    fun onAppStart(context: Context) {
        autoLogin(context)
    }

    fun autoLogin(context: Context) {
//        val refreshToken = getRefreshToken(context)
//
//        // JWT 토큰이 존재하면 기존 유저로 간주하고 서버에 자동 로그인 요청을 보냅니다.
//        if (jwtToken != null) {
//            // Retrofit2를 사용하여 백엔드에 자동 로그인 요청을 보내는 코드를 작성합니다.
//            // (Retrofit2를 사용하는 방법은 여기서 자세하게 설명하지 않습니다.)
//
//            // 서버로 요청을 보내고 응답을 받는 코드를 구현하고, 응답에 따라 로그인 성공/실패를 처리합니다.
//            // 예를 들면, 토큰의 유효성을 확인하고, 유효하다면 메인 액티비티로 이동하도록 합니다.
//        } else {
//            // JWT 토큰이 없는 경우에는 신규 유저로 간주하고 로그인 화면으로 이동하도록 합니다.
//            // (기존 유저와 신규 유저 구분을 위한 로직이 들어갑니다.)
//        }
    }

    private fun getAccessToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    private fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }

    private fun saveAccessToken(accessToken: String) {
        sharedPreferences.edit().putString("access_token", accessToken).apply()
    }

    private fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
    }

    private fun getUserData(accessToken: String) {
        val app = applicationContext as GlobalApplication

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = app.apiService.getUserData(accessToken)

                app.email = response.email
                app.nickname = response.nickname
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error during API call: ${e.message}", e)
            }
        }
    }
}