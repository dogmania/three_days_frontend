package com.example.threedays

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.threedays.databinding.ActivityAddHabitFirstBinding

class AddHabitFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitFirstBinding

    private lateinit var buttons : Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons = arrayOf(
            binding.btnDay1,
            binding.btnDay2,
            binding.btnDay3,
            binding.btnDay4,
            binding.btnDay5,
            binding.btnDay6,
            binding.btnDay7
        )

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                selectButton(buttons, i)
            }
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnComplete.setOnClickListener {
            val intent = Intent(this, AddHabitSecondActivity::class.java)
            startActivity(intent)
        }
    }

    private fun selectButton(buttons : Array<Button>, index : Int) {
        for (i in buttons.indices) {
            buttons[i].setBackgroundResource(R.drawable.btn_white_background)
        }

        buttons[index].setBackgroundResource(R.drawable.btn_style_round_green)
    }
}