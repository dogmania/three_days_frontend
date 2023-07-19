package com.example.threedays.view.sns

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.HabitCertification
import com.example.threedays.R
import com.example.threedays.databinding.ItemHabitUploadBinding
import com.example.threedays.databinding.ItemImageBinding

class MyHabitAdapter(private val habitCertification: List<HabitCertification>, val nickname: String, val habitName: String)
    : RecyclerView.Adapter<MyHabitAdapter.MyHabitViewHolder>() {

    inner class MyHabitViewHolder(binding: ItemHabitUploadBinding): RecyclerView.ViewHolder(binding.root) {
        val nickname = binding.nickname
        val habitName = binding.habitName
        val reviewText = binding.reviewText
        val reviewLayout = binding.reviewLayout
        val viewPager = binding.viewPager

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

            val imageAdapter = ImageAdapter(habitCertificationData.image!!)
            holder.viewPager.adapter = imageAdapter
            holder.reviewText.text = habitCertificationData.habitReview
            holder.reviewLayout.visibility = View.VISIBLE

            if (habitCertificationData.grade != 0) {
                val filledStarDrawable = R.drawable.ic_star_filled
                val emptyStarDrawable = R.drawable.ic_star_empty

                for (i in 0 until habitCertificationData.grade) {
                    val starImageView = holder.reviewLayout.getChildAt(i) as ImageView
                    starImageView.setImageResource(filledStarDrawable)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return habitCertification.size
    }

    inner class ImageAdapter(private val imageUriList: List<Uri>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        inner class ImageViewHolder(binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root) {
            val imageView = binding.imageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ImageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageUri = imageUriList[position]

            holder.imageView.setImageURI(imageUri)


        }

        override fun getItemCount(): Int {
            return imageUriList.size
        }
    }
}