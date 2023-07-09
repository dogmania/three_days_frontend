package com.example.threedays

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.databinding.ItemHabitModificationBinding

class HabitModificationAdapter(private val habits: MutableList<Habit>, private val context: Context)
    : RecyclerView.Adapter<HabitModificationAdapter.MyViewHolder> () {


    inner class MyViewHolder(binding : ItemHabitModificationBinding) : RecyclerView.ViewHolder(binding.root) {
        val habitName = binding.habitName
        val period = binding.period
        val achievementRate = binding.achievementRate
        val combo = binding.combo
        val numOfAchievement = binding.numberOfAchievements
        val btnPublic = binding.btnPublic
        val itemLayout = binding.itemLayout

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemHabitModificationBinding = ItemHabitModificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val habitData = habits[position]

        holder.habitName.text = habitData.habitName
        holder.period.text = "🗓️목표: 주 " + habitData.period + "회 " + "달성"
        holder.achievementRate.text = habitData.achievementRate.toString() + "%"
        holder.combo.text = habitData.combo.toString() + "combo"
        holder.numOfAchievement.text = habitData.numOfAchievement.toString() + "/" + habitData.period.toString()

        holder.btnPublic.isChecked = habitData.disclosure!!

        if (habitData.disclosure == false) {
            holder.itemLayout.setBackgroundResource(R.drawable.round_frame_yellow)
            holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_private)
        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.round_frame_white)
            holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_public)
        }

        holder.btnPublic.setOnCheckedChangeListener { buttonView, isChecked ->
            habits[position] = habitData.copy(disclosure = isChecked)

            if (isChecked) {
                holder.itemLayout.setBackgroundResource(R.drawable.round_frame_white)
                holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_public)
            } else {
                holder.itemLayout.setBackgroundResource(R.drawable.round_frame_yellow)
                holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_private)
            }
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }
}