package com.example.threedays

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.databinding.ItemHabitModificationBinding

class HabitModificationAdapter(private val habits: MutableList<com.example.threedays.api.Habit>, private val listener: HabitUpdateListener)
    : RecyclerView.Adapter<HabitModificationAdapter.MyViewHolder> () {

    private val selectedHabits = mutableListOf<com.example.threedays.api.Habit>()

    inner class MyViewHolder(binding : ItemHabitModificationBinding) : RecyclerView.ViewHolder(binding.root) {
        val habitName = binding.habitName
        val period = binding.period
        val achievementRate = binding.achievementRateNum
        val combo = binding.combo
        val currentAchievement = binding.currentAchievements
        val numOfAchievement = binding.numberOfAchievements
        val btnPublic = binding.btnPublic
        val itemLayout = binding.itemLayout
        val checkBox = binding.checkBox
        val root = binding.root
        val btnModify = binding.btnModify

        init {
            checkBox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemModel = habits[position]
                    itemModel.isChecked = checkBox.isChecked
                    if (itemModel.isChecked) {
                        checkBox.setBackgroundResource(R.drawable.ic_checkbox_checked)
                        addSelectedHabit(itemModel)
                    } else {
                        checkBox.setBackgroundResource(R.drawable.ic_checkbox_unchecked)
                        removeSelectedHabit(itemModel)
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding : ItemHabitModificationBinding = ItemHabitModificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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

        holder.btnPublic.isChecked = habitData.visible

        if (habitData.visible == false) {
            holder.itemLayout.setBackgroundResource(R.drawable.round_frame_yellow)
            holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_private)
        } else {
            holder.itemLayout.setBackgroundResource(R.drawable.round_frame_white)
            holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_public)
        }

        holder.btnPublic.setOnCheckedChangeListener { buttonView, isChecked ->
            habits[position] = habitData.copy(visible = isChecked)

            if (isChecked) {
                holder.itemLayout.setBackgroundResource(R.drawable.round_frame_white)
                holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_public)
            } else {
                holder.itemLayout.setBackgroundResource(R.drawable.round_frame_yellow)
                holder.btnPublic.setBackgroundResource(R.drawable.ic_btn_private)
            }
        }

        holder.btnModify.setOnClickListener {
            onEditButtonClicked(habitData)
        }
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    fun addSelectedHabit(habit: com.example.threedays.api.Habit) {
        selectedHabits.add(habit)
    }

    fun removeSelectedHabit(habit: com.example.threedays.api.Habit) {
        selectedHabits.remove(habit)
    }

    fun getSelectedHabits(): List<com.example.threedays.api.Habit> {
        return selectedHabits
    }

    fun onEditButtonClicked(habit: com.example.threedays.api.Habit) {
        listener.onModifyButtonClick(habit)
    }
}