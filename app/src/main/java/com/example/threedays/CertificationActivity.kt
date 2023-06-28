package com.example.threedays

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.threedays.databinding.ActivityCertificationBinding

class CertificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCertificationBinding

    companion object {
        private const val GALLERY_REQUEST_CODE = 123 //다른 요청들과 구분하기 위해 갤러리 오픈에 대해 요청 코드값 부여
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habitName = intent.getStringExtra("habitName")!!
        binding.habitName.text = habitName

        binding.btnPlanArrangement.setOnClickListener {
            addEditText()
        }

        binding.btnAddImage.setOnClickListener {
            openGallery()
        }

        binding.btnDifficulty.setOnClickListener {
            addReviewStars()
        }

        binding.planArrangementFrame.visibility = View.GONE
    }

    private fun addEditText() {
        binding.planArrangementFrame.visibility = View.VISIBLE

        val editText = EditText(this)
        editText.setBackgroundColor(Color.TRANSPARENT)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            width = 200.dpToPx()
            height = 100.dpToPx()
            leftMargin = 16.dpToPx()
            topMargin = 16.dpToPx()
        }

        binding.planArrangementFrame.addView(editText, layoutParams)
    }

    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                val imageView = ImageView(this)
                val layoutParams = LinearLayout.LayoutParams(
                    100.dpToPx(),
                    100.dpToPx()
                ).apply {
                    setMargins(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
                }

                imageView.setImageURI(it)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP

                binding.photoLayout.addView(imageView, layoutParams)

                // 리니어레이아웃이 사진이 있는 경우에만 보이도록 설정
                binding.photoLayout.visibility = LinearLayout.VISIBLE
            }
        }
    }

    private fun addReviewStars() {
        binding.reviewLayout.removeAllViews()
    }
}