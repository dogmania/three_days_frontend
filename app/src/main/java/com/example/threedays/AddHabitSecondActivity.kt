package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.threedays.databinding.ActivityAddHabitSecondBinding

class AddHabitSecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitSecondBinding
    private val default = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val period = intent.getIntExtra("period", default)
        val nickname = intent.getStringExtra("nickname")!!

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnComplete.setOnClickListener {
            checkEditText(period, nickname)
        }
    }

    private fun checkEditText(period : Int, nickname : String) {
        val habitName = binding.setHabitEdittext.text.toString()

        if (habitName.isBlank()) {
            Toast.makeText(this, "습관을 설정해주세요",
            Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, AddHabitThirdActivity::class.java)
            intent.putExtra("period", period)
            intent.putExtra("nickname", nickname)
            intent.putExtra("habitName", habitName)
            startActivity(intent)
        }
    }
}