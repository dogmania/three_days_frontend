package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threedays.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val user = userManager.getUser("손현수")

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (user?.habits.isNullOrEmpty()) {
            setFragement()
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddHabitFirstActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setFragement() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, EmptyHabitFragment())
        transaction.commit()
    }
}