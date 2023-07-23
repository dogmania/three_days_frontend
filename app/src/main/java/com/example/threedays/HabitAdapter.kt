package com.example.threedays

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.databinding.ItemHabitBinding

class HabitAdapter(private val habits: List<com.example.threedays.api.Habit>, private val context: Context, private val nickname: String)
    : RecyclerView.Adapter<HabitAdapter.MyViewHolder> () {

    inner class MyViewHolder(binding : ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        val habitName = binding.habitName
        val period = binding.period
        val achievementRate = binding.achievementRateNum
        val combo = binding.combo
        val currentAchievement = binding.currentAchievements
        val numOfAchievement = binding.numberOfAchievements
        val btnCertification = binding.btnCertification

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemHabitBinding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val habitData = habits[position]

        holder.habitName.text = habitData.title
        holder.period.text = "🗓️목표: 주 " + habitData.duration + "회 " + "달성"
        holder.achievementRate.text = habitData.achievementRate.toString()
        holder.combo.text = habitData.comboCount.toString()
        holder.currentAchievement.text = habitData.achievementCount.toString()
        holder.numOfAchievement.text = habitData.duration.toString()

        holder.btnCertification.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, CertificationActivity::class.java)
            intent.putExtra("habitName", habitData.title) // 선택된 습관의 이름 전달
            intent.putExtra("nickname", nickname)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }
}