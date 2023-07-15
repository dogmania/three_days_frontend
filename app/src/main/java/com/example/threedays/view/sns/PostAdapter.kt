package com.example.threedays.view.sns

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.HabitCertification
import com.example.threedays.databinding.ItemHabitUploadBinding

class PostAdapter(private val habitCertification: List<HabitCertification>): RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    inner class PostViewHolder(binding: ItemHabitUploadBinding) : RecyclerView.ViewHolder(binding.root) {
        val profileImage = binding.profileImage
        val nickname = binding.nickname
        val viewPager = binding.viewPager
        val day = binding.day
        val reviewLayout = binding.reviewLayout
        val habitName = binding.habitName
        val reviewText = binding.reviewText

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemHabitUploadBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val habitCertificationData = habitCertification[position]
    }

    override fun getItemCount(): Int {
        return habitCertification.size
    }
}