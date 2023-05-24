package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton
import com.example.threedays.databinding.ActivityAddHabitThirdBinding

class AddHabitThirdActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddHabitThirdBinding
    private lateinit var buttons : Array<ToggleButton>
    private var disclosure : Boolean = false
    private var movable : Boolean = false
    private val default = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons = arrayOf(binding.btnYes, binding.btnNo)

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                selectButton(buttons, i)
            }
        }

        val period = intent.getIntExtra("period", default)
        val habitName = intent.getStringExtra("habitName")!!
        val nickname = intent.getStringExtra("nickname")!!

        binding.btnComplete.setOnClickListener {
            insertHabit(period, habitName, nickname)
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
            0 -> disclosure = true
            1 -> disclosure = false
        }
    }

    private fun insertHabit(period : Int, habitName : String, nickname : String) {
        for (i in buttons.indices) {
            if(buttons[i].isChecked) {
                movable = true
            }
        }

        if (movable) {
            val habit = Habit(period, habitName, disclosure)
            val user = userManager.getUser(nickname)!!
            user.habits.add(habit)

            val intent = Intent(this, HabitFormationCompleteActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)

        } else {
            Toast.makeText(this, "공개 여부를 선택해주세요",
            Toast.LENGTH_SHORT).show()
        }
    }
}