package com.example.threedays

import android.net.Uri

data class HabitCertification(
    val image: List<Uri>,
    val habitReview: String?,
    val grade: Int
)
