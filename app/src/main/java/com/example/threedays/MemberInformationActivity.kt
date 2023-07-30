package com.example.threedays

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.threedays.api.UserInfo
import com.example.threedays.databinding.ActivityMemberInformationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemberInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemberInformationBinding
    private lateinit var keywordEditTextContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberInformationBinding.inflate(layoutInflater)

        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        keywordEditTextContainer = binding.keywordEdittextContainer

        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val currentEdittextCount = keywordEditTextContainer.childCount
            val editText = EditText(this).apply {
                val marginTop = resources.getDimension(R.dimen.keyword_edittext_margin_top).toInt()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // layout_width
                    LinearLayout.LayoutParams.WRAP_CONTENT // layout_height
                ).apply {
                    topMargin = marginTop
                }

                setBackgroundResource(R.drawable.nickname_edittext) // background
                hint = resources.getString(R.string.keyword_hint_edt) // hint
                inputType = InputType.TYPE_CLASS_TEXT
            }

            if (currentEdittextCount < 5) {//현재 edit text 개수가 5개 이하인 경우에만 실행
                keywordEditTextContainer.addView(editText, currentEdittextCount)
                if (currentEdittextCount == 4) binding.btnAdd.visibility = View.GONE
            } else {
                binding.btnAdd.visibility = View.GONE //edit text 개수가 5개가 되면 더이상 추가하기 버튼이 보이지 않음
            }
        }

        binding.btnComplete.setOnClickListener {
            insertUser()
        }
    }

    private fun insertUser() {
        val app = applicationContext as GlobalApplication

        val nickname = binding.nicknameEdittext.text.toString()
        sharedPreferences.edit().putString("nickname", nickname).apply()
        val email = sharedPreferences.getString("email", null)!!

        val keywords: MutableList<String> = mutableListOf()

        for (i in 0 until keywordEditTextContainer.childCount) { // 추가한 EditText 개수만큼 반복
            val keywordEditText = keywordEditTextContainer.getChildAt(i) as EditText // EditText 위치한 곳부터 가져옴
            val keyword = keywordEditText.text.toString() // EditText 에서 입력된 문자열을 가져옴
            if (keyword.isNotBlank()) {//비어있지 않은 경우에만 keywords 에 추가
                keywords.add(keyword)
            }
        }

        if (nickname.isBlank() || keywords.isEmpty()) {
            Toast.makeText(this, "모든 항목을 채워주세요.",
                Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = app.apiService.saveProfile(UserInfo(email, keywords, nickname))
                } catch (e: Exception) {
                    Log.e("MemberInformationActivity", "Error during saveProfile API call", e)
                }
            }

            Toast.makeText(this, "완료되었습니다.",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}