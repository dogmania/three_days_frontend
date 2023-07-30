package com.example.threedays

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.threedays.api.CertifyData
import com.example.threedays.databinding.ActivityCertificationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class CertificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCertificationBinding
    private val calendar = Calendar.getInstance()
    private var selectedImages = mutableListOf<Uri>()
    private var habitReview: String? = null
    private var grade: Int = 0
    private val default = -1L

    companion object {
        private const val GALLERY_REQUEST_CODE = 123 //다른 요청들과 구분하기 위해 갤러리 오픈에 대해 요청 코드값 부여
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habitName = intent.getStringExtra("habitName")!!
        val id = intent.getLongExtra("id", default)
        val app = applicationContext as GlobalApplication
        val imagesParts: MutableList<MultipartBody.Part> = mutableListOf()
        binding.habitName.text = habitName

        binding.currentMonth.text = (calendar.get(Calendar.MONTH) + 1).toString() + resources.getString(R.string.month)
        binding.currentDay.text = calendar.get(Calendar.DATE).toString() + resources.getString(R.string.day)

        binding.btnPlanArrangement.setOnClickListener {
            addEditText()
        }

        binding.btnAddImage.setOnClickListener {
            verifyStoragePermissions(this)

            openGallery()
        }

        binding.btnDifficulty.setOnClickListener {
            addReviewStars()
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.planArrangementFrame.visibility = View.GONE
        binding.reviewLayout.visibility = View.GONE

        binding.btnComplete.setOnClickListener {
            if (habitReview == null) {
                habitReview = ""
            }
            val data = CertifyData(grade, habitReview!!)

            for (uri in selectedImages) {
                val compressedImageFile = compressImageAndRotateIfNeeded(this@CertificationActivity, uri)
                compressedImageFile?.let {
                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                    val imagePart = MultipartBody.Part.createFormData("images", it.name, requestFile)
                    imagesParts.add(imagePart)
                }
            }

            CoroutineScope(Dispatchers.IO).launch {
                Log.d("imagesParts", "$imagesParts")
                try {
                    app.apiService.certifyHabit(
                        id,
                        data.level.toString().toTextPlainRequestBody(),
                        data.review.toTextPlainRequestBody(),
                        imagesParts
                    )
                } catch (e: Exception) {
                    Log.e("CertificationActivity", "Error during certifyHabit API call", e)
                }
            }

            val intent = Intent(this, HabitCertificationCompleteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addEditText() {
        binding.planArrangementFrame.visibility = View.VISIBLE

        val editText = EditText(this)
        editText.setBackgroundColor(Color.TRANSPARENT)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            width = 350.dpToPx()
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
            if (starView.tag == "filled") {
                count++
            }
            grade = count
        }
    }


    fun String.toTextPlainRequestBody(): RequestBody {
        return this.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    fun verifyStoragePermissions(activity: Activity) {
        val permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    fun getRotatedBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap?, fileName: String?): File? {
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (directory != null) {
            val file = File(directory, fileName)
            return try {
                FileOutputStream(file).use { out ->
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
                }
                file
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
        return null
    }

    private fun compressImageAndRotateIfNeeded(context: Context, uri: Uri): File? {
        try {
            val rotatedBitmap = getRotatedBitmap(context, uri)
            val randomRotatedFileName = "${UUID.randomUUID()}.jpg"
            val rotatedImagePath = saveBitmapToFile(context, rotatedBitmap, randomRotatedFileName)
            rotatedBitmap?.recycle() // Recycle the rotated bitmap as it is no longer needed.

            if (rotatedImagePath != null) {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeFile(rotatedImagePath.path, options)

                val maxWidth = 800 // 압축할 이미지의 최대 폭 설정
                val maxHeight = 800 // 압축할 이미지의 최대 높이 설정
                var scale = 1
                while (options.outWidth / scale / 2 >= maxWidth && options.outHeight / scale / 2 >= maxHeight) {
                    scale *= 2
                }

                val bitmapOptions = BitmapFactory.Options()
                bitmapOptions.inSampleSize = scale
                val bitmap = BitmapFactory.decodeFile(rotatedImagePath.path, bitmapOptions)

                // Generate a random filename using UUID
                val randomFileName = "${UUID.randomUUID()}.jpg"
                val compressedImageFile = File(context.cacheDir, randomFileName)

                val fileOutputStream = FileOutputStream(compressedImageFile)
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, fileOutputStream) // 이미지 압축 품질 설정 (0~100)
                fileOutputStream.flush()
                fileOutputStream.close()

                bitmap?.recycle() // Recycle the bitmap as it is no longer needed.

                return compressedImageFile
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}