package com.example.threedays

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.threedays.databinding.ActivityMemberInformationBinding

class memberInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemberInformationBinding
    private lateinit var keywordEditTextContainer: LinearLayout

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    lateinit var userManager : UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberInformationBinding.inflate(layoutInflater)
        keywordEditTextContainer = binding.keywordEdittextContainer

        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val currentEditTextCount = keywordEditTextContainer.childCount
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

            if (currentEditTextCount < 5) {//현재 edittext의 개수가 5개 이하인 경우에만 실행
                keywordEditTextContainer.addView(editText, currentEditTextCount)
                if (currentEditTextCount == 4) binding.btnAdd.visibility = View.GONE
            } else {
                binding.btnAdd.visibility = View.GONE//edittext의 개수가 5개가 되면 더이상 추가하기 버튼이 보이지 않음
            }
        }

        binding.btnComplete.setOnClickListener {
            insertUser()
        }
    }

    private fun insertUser() {
        val nickname = binding.nicknameEdittext.text.toString()
        var keywords: MutableList<String> = mutableListOf()

        for (i in 0 until keywordEditTextContainer.childCount) { // 추가한 EditText의 개수만큼 반복
            val keywordEditText = keywordEditTextContainer.getChildAt(i) as EditText // edittext가 위치한 곳부터 가져옴
            val keyword = keywordEditText.text.toString() // EditText에서 입력된 문자열을 가져옴
            if (keyword.isNotBlank()) {//비어있지 않은 경우에만 keywords에 추가
                keywords.add(keyword)
            }
        }

        if (nickname.isBlank() || keywords.isEmpty()) {
            Toast.makeText(this, "모든 항목을 채워주세요.",
                Toast.LENGTH_SHORT).show()
        } else {
            Thread {
                val user = User(nickname, keywords)
                userManager.addUser(user)
                runOnUiThread{
                    Toast.makeText(this, "완료되었습니다.",
                        Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }
    }
}