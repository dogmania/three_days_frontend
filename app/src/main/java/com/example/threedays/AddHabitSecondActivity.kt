package com.example.threedays

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threedays.databinding.ActivityAddHabitSecondBinding

class AddHabitSecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitSecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
}