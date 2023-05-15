package com.example.threedays

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

        if (user?.hobits.isNullOrEmpty()) {
            setFragement()
        }
    }

    private fun setFragement() {
        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, EmptyHabitFragment())
        transaction.commit()
    }
}