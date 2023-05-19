package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.threedays.databinding.ActivityMainBinding
import com.example.threedays.databinding.FragmentEmptyHabitBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nickname : String = intent.getStringExtra("nickname")!! //onCreate 함수 안에서 변수를 가져와야 함
        val user = userManager.getUser(nickname)

        if (user?.habits.isNullOrEmpty()) {
            setFragment(nickname)
        }//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddHabitFirstActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
        }
    }

    private fun setFragment(nickname: String) {
        val fragment = EmptyHabitFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }


        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.main_frame, fragment)
        transaction.commit()
    }
}