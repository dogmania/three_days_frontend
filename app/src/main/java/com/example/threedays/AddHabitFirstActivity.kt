package com.example.threedays

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import com.example.threedays.databinding.ActivityAddHabitFirstBinding

open class AddHabitFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddHabitFirstBinding

    private lateinit var buttons : Array<ToggleButton>
    var habit  = Habit()
    var movable = false

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
            for (i in buttons.indices) {
                if (buttons[i].isChecked) {
                    movable = true
                }
            }

            if (movable) {
                val intent = Intent(this, AddHabitSecondActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "기간을 선택해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectButton(buttons : Array<ToggleButton>, index : Int) {
        for (i in buttons.indices) {
            buttons[i].isChecked = false
            buttons[i].setBackgroundResource(R.drawable.btn_white_background)
        }

        buttons[index].isChecked = true
        buttons[index].setBackgroundResource(R.drawable.btn_style_round_green)

        when (index) {
            0 -> habit.copy(1)
            1 -> habit.copy(2)
            2 -> habit.copy(3)
            3 -> habit.copy(4)
            4 -> habit.copy(5)
            5 -> habit.copy(6)
            6 -> habit.copy(7)
        }
    }
}