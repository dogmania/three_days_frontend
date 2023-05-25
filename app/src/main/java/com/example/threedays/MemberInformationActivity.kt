package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.threedays.databinding.ActivityMemberInformationBinding

val userManager = UserManager.getInstance()

class MemberInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemberInformationBinding
    private lateinit var keywordEditTextContainer: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberInformationBinding.inflate(layoutInflater)
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
        val nickname = binding.nicknameEdittext.text.toString()
        val keywords: MutableList<String> = mutableListOf()
        val habits = mutableListOf<Habit>()

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
            Thread {//백그라운드 스레드에서 유저 정보를 저장하는 작업을 실행
                val user = User(nickname, keywords, habits)
                userManager.addUser(user)
                runOnUiThread{//유저 정보를 저장하는 과정이 끝나면 토스트 메시지로 완료되었음을 사용자에게 전달
                    Toast.makeText(this, "완료되었습니다.",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("nickname", nickname)
                    startActivity(intent)
                    finish()
                }
            }.start()
        }
    }
}