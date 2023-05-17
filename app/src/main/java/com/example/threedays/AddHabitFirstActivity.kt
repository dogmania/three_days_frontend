package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threedays.databinding.ActivityAddHabitFirstBinding

class AddHabitFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnComplete.setOnClickListener {
            val intent = Intent(this, AddHabitSecondActivity::class.java)
            startActivity(intent)
        }
    }
}