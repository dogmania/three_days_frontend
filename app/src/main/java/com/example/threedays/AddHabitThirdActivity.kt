package com.example.threedays

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import com.example.threedays.api.CreateHabit
import com.example.threedays.databinding.ActivityAddHabitThirdBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddHabitThirdActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddHabitThirdBinding
    private lateinit var buttons : Array<ToggleButton>
    private var disclosure : Boolean = false
    private var movable : Boolean = false
    private val default = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHabitThirdBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
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
            insertHabit(period, habitName)
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

    private fun insertHabit(period : Int, habitName : String) {
        for (i in buttons.indices) {
            if(buttons[i].isChecked) {
                movable = true
            }
        }

        if (movable) {
            val app = applicationContext as GlobalApplication
            val email = sharedPreferences.getString("email", null)!!

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    app.apiService.createHabit(
                        CreateHabit(
                        period,
                        email,
                        habitName,
                        disclosure
                        )
                    )
                } catch (e:Exception) {
                    Log.e("AddHabitThirdActivity", "Error during createHabit API call", e)
                }

                withContext(Dispatchers.Main) {
                    val intent = Intent(this@AddHabitThirdActivity, HabitFormationCompleteActivity::class.java)
                    startActivity(intent)
                }
            }

        } else {
            Toast.makeText(this, "공개 여부를 선택해주세요",
            Toast.LENGTH_SHORT).show()
        }
    }
}