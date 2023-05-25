package com.example.threedays

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threedays.databinding.ActivityCertificationBinding

class CertificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCertificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val habitName = intent.getStringExtra("habitName")!!
        binding.habitName.text = habitName
    }
}