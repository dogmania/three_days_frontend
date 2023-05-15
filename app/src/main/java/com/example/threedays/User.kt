package com.example.threedays

data class User(
    val nickname: String,
    val keywords : MutableList<String>,
    val hobits : MutableList<Hobit>
)