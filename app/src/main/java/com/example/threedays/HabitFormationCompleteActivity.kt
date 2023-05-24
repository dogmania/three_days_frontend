package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threedays.databinding.ActivityHabitFormationCompleteBinding

class HabitFormationCompleteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHabitFormationCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHabitFormationCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nickname = intent.getStringExtra("nickname")!!

        binding.btnComplete.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }
    }
}