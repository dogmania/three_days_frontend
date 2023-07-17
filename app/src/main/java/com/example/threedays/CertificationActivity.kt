package com.example.threedays

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.threedays.databinding.ActivityCertificationBinding
import java.util.Calendar

class CertificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCertificationBinding
    private val calendar = Calendar.getInstance()
    private var selectedImages = mutableListOf<Uri>()
    private var habitReview: String? = null
    private var grade: Int = 0
    private lateinit var name: String
    private lateinit var habit: String

    companion object {
        private const val GALLERY_REQUEST_CODE = 123 //다른 요청들과 구분하기 위해 갤러리 오픈에 대해 요청 코드값 부여
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habitName = intent.getStringExtra("habitName")!!
        val nickname = intent.getStringExtra("nickname")!!
        name = nickname
        habit = habitName
        binding.habitName.text = habitName

        binding.currentMonth.text = calendar.get(Calendar.MONTH + 1).toString() + resources.getString(R.string.month)
        binding.currentDay.text = calendar.get(Calendar.DATE).toString() + resources.getString(R.string.day)

        binding.btnPlanArrangement.setOnClickListener {
            addEditText()
        }

        binding.btnAddImage.setOnClickListener {
            openGallery()
        }

        binding.btnDifficulty.setOnClickListener {
            addReviewStars()
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }

        binding.planArrangementFrame.visibility = View.GONE
        binding.reviewLayout.visibility = View.GONE

        binding.btnComplete.setOnClickListener {
            if (selectedImages.isEmpty()) {
                val defaultImageUri = Uri.parse("android.resource://com.example.threedays/" + R.drawable.default_image)
                selectedImages.add(defaultImageUri)
            }
            completeCertification(selectedImages, habitReview, grade)
            val intent = Intent(this, HabitCertificationCompleteActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }
    }

    private fun completeCertification(selectedImages: MutableList<Uri>, habitReview: String?, grade: Int) {
        val user = userManager.getUser(name)!!
        val habit = user.habits.find {it.habitName == habit}!!
        val certification = HabitCertification(selectedImages, habitReview, grade)

        habit.certification?.add(certification)
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

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 이전 텍스트 변경 전에 호출될 코드
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트가 변경될 때 호출될 코드
                habitReview = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // 텍스트 변경 후에 호출될 코드
            }
        })
    }

    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
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

            if (data.clipData != null) {
                // 다중 이미지 선택
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    selectedImages.add(imageUri)
                }
            } else if (data.data != null) {
                // 단일 이미지 선택
                val imageUri = data.data
                selectedImages.add(imageUri!!)
            }
        } else {
            val defaultImageUri = Uri.parse("android.resource://com.example.threedays/" + R.drawable.default_image)
            selectedImages.add(defaultImageUri)
        }
    }

    private fun addReviewStars() {
        binding.reviewLayout.visibility = View.VISIBLE
        binding.reviewLayout.removeAllViews()

        for (i in 1..5) {
            val starImageView = ImageView(this)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.marginEnd = 16.dpToPx()
            //LinearLayout.LayoutParams는 LinearLayout의 내부에서 사용되는 레이아웃 속성을 정의하는 클래스이다.
            //이 클래스의 생성자는 두개의 매개변수를 요구하고 자식 레이아웃의 width, height 값을 매개변수로 사용한다.

            starImageView.layoutParams = layoutParams
            starImageView.setImageResource(R.drawable.ic_star_empty)
            starImageView.tag = "empty"
            //각각의 이미지뷰에 대해서 태그를 이용하여 채워진 별인지 비어있는 별인지 구분
            starImageView.isClickable = true
            starImageView.setOnClickListener { view ->
                handleStarClick(view)
            }

            binding.reviewLayout.addView(starImageView)
        }
    }

    private fun handleStarClick(starView: View) {
        val clickedStarIndex = binding.reviewLayout.indexOfChild(starView)
        val filledStarDrawable = R.drawable.ic_star_filled
        val emptyStarDrawable = R.drawable.ic_star_empty
        var count = 0

        for (i in 0 until clickedStarIndex) {
            val starImageView = binding.reviewLayout.getChildAt(i) as ImageView
            starImageView.setImageResource(filledStarDrawable)
            starImageView.tag = "filled"
        }

        if (starView.tag == "filled") {
            (starView as ImageView).setImageResource(R.drawable.ic_star_empty)
            starView.tag = "empty"
        } else {
            (starView as ImageView).setImageResource(R.drawable.ic_star_filled)
            starView.tag = "filled"
        }

        for (i in clickedStarIndex + 1 until binding.reviewLayout.childCount) {
            val starImageView = binding.reviewLayout.getChildAt(i) as ImageView
            starImageView.setImageResource(emptyStarDrawable)
            starImageView.tag = "empty"
        }//클릭된 별 이후에 있는 별들을 비우는 코드

        for (i in 0 until binding.reviewLayout.childCount) {
            count = 0
            if (starView.tag == "filled") {
                count++
            }
            grade = count
        }
    }
}