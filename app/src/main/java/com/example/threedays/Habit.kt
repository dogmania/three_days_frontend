package com.example.threedays

data class Habit (
    val id: Long,
    val period : Int? = null,
    val habitName : String,
    val disclosure : Boolean? = null,
    val certification: MutableList<HabitCertification>,
    val achievementRate : Int = 0,
    val combo : Int = 0,
    val numOfAchievement : Int = 0
        )