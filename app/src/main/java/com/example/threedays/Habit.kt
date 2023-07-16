package com.example.threedays

data class Habit (
    val period : Int? = null,
    val habitName : String,
    val disclosure : Boolean? = null,
    val achievementRate : Int = 0,
    val combo : Int = 0,
    val numOfAchievement : Int = 0
        )