package com.example.threedays

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.databinding.ItemHabitBinding

class HabitAdapter(private val habits: List<Habit>, private val context: Context)
    : RecyclerView.Adapter<HabitAdapter.MyViewHolder> () {

    inner class MyViewHolder(binding : ItemHabitBinding) : RecyclerView.ViewHolder(binding.root) {
        val habitName = binding.habitName
        val period = binding.period
        val achievementRate = binding.achievementRate
        val combo = binding.combo
        val numOfAchievement = binding.numberOfAchievements

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemHabitBinding = ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val habitData = habits[position]

        holder.habitName.text = habitData.habitName
        holder.period.text = "🗓️목표: 주 " + habitData.period + "회 " + "달성"
        holder.achievementRate.text = habitData.achievementRate.toString() + "%"
        holder.combo.text = habitData.combo.toString() + "combo"
        holder.numOfAchievement.text = habitData.numOfAchievement.toString() + "/" + habitData.period.toString()


    }

    override fun getItemCount(): Int {
        return habits.size
    }
}