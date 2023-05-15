package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.threedays.databinding.ActivityFirstPageBinding

class FirstPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirstPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartKakao.setOnClickListener {
            val intent = Intent(this, MemberInformationActivity::class.java)
            startActivity(intent)
        }
    }
}