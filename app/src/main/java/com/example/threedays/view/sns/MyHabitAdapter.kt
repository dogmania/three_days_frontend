package com.example.threedays.view.sns

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.threedays.HabitCertification
import com.example.threedays.R
import com.example.threedays.api.Certification
import com.example.threedays.databinding.ItemHabitUploadBinding
import com.example.threedays.databinding.ItemImageBinding
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class MyHabitAdapter(private val habitCertification: List<Certification>, val nickname: String, val habitName: String, val context: Context, private val createdDate: String, private val profileImageUrl: String)
    : RecyclerView.Adapter<MyHabitAdapter.MyHabitViewHolder>() {

    inner class MyHabitViewHolder(binding: ItemHabitUploadBinding): RecyclerView.ViewHolder(binding.root) {
        val nickname = binding.nickname
        val habitName = binding.habitName
        val reviewText = binding.reviewText
        val reviewLayout = binding.reviewLayout
        val viewPager = binding.viewPager
        val profileImage = binding.profileImage
        val day = binding.day
        val certifiedDate = binding.certifiedDate

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHabitViewHolder {
        val binding = ItemHabitUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHabitViewHolder, position: Int) {
        holder.nickname.text = nickname

        if (habitCertification != null) {
            val habitCertificationData = habitCertification[position]

            val imageAdapter = ImageAdapter(habitCertificationData.imagUrls)
            holder.viewPager.adapter = imageAdapter
            holder.reviewText.text = habitCertificationData.review

            if (habitCertificationData.level != 0) {
                holder.reviewLayout.visibility = View.VISIBLE

                holder.reviewLayout.removeAllViews()

                for (i in 1..5) {
                    val starImageView = ImageView(holder.itemView.context)
                    val layoutParams = LinearLayout.LayoutParams(
                        20.dpToPx(), // 가로 크기 20dp
                        18.dpToPx()  // 세로 크기 18dp
                    )
                    layoutParams.marginEnd = 2.dpToPx()

                    starImageView.layoutParams = layoutParams

                    // level 변수에 따라 이미지를 바꿔줌
                    if (i <= habitCertificationData.level) {
                        starImageView.setImageResource(R.drawable.ic_star_filled)
                    } else {
                        starImageView.setImageResource(R.drawable.ic_star_empty)
                    }

                    holder.reviewLayout.addView(starImageView)
                }
            }
            holder.habitName.text = habitName
            holder.reviewText.text = habitCertificationData.review
            val dateSubString = habitCertificationData.certifiedDate.substring(0, 10)
            holder.certifiedDate.text = dateSubString.replace("-", ".")
            val startDate = createdDate.substring(0, 10)
            val dateDifference = calculateDateDifference(startDate, dateSubString) + 1
            holder.day.text = "D + " + dateDifference.toString()
            Glide.with(holder.itemView.context)
                .load(profileImageUrl)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(holder.profileImage)
        }
    }

    override fun getItemCount(): Int {
        return habitCertification.size
    }

    inner class ImageAdapter(private val imageUrlList: List<String>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        inner class ImageViewHolder(binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
            val imageView = binding.imageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ImageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageUrl = imageUrlList[position]

            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imageView)
        }

        override fun getItemCount(): Int {
            return imageUrlList.size
        }
    }

    private fun Int.dpToPx(): Int {
        val scale = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    fun calculateDateDifference(startDateStr: String, endDateStr: String): Long {
        val startDate = LocalDate.parse(startDateStr)
        val endDate = LocalDate.parse(endDateStr)
        return ChronoUnit.DAYS.between(startDate, endDate)
    }
}