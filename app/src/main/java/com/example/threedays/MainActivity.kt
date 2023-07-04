package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.threedays.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var habits : MutableList<Habit>

    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nickname : String = intent.getStringExtra("nickname")!! //onCreate 함수 안에서 변수를 가져와야 함
        val user = userManager.getUser(nickname)!!
        habits = user.habits

        if (habits.isNullOrEmpty()) {
            setEmptyFragment(nickname)//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정
        } else {
            setFragment(nickname)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddHabitFirstActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        val nickname : String = intent.getStringExtra("nickname")!! //onCreate 함수 안에서 변수를 가져와야 함
        val user = userManager.getUser(nickname)!!
        habits = user.habits

        if (habits.isNullOrEmpty()) {
            setEmptyFragment(nickname)//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정
        } else {
            setFragment(nickname)
        }
    }

    private fun setEmptyFragment(nickname: String) {
        val fragment = EmptyHabitFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
            .add(R.id.sub_frame, fragment)
        transaction.commit()
    }

    private fun setFragment(nickname: String) {
        val fragment = HabitFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.sub_frame, fragment)
        transaction.commit()
    }

    fun showModifyFragment(nickname: String) {
        binding.subFrame.visibility = View.GONE
        binding.navigationLayout.visibility = View.GONE

        val habitModifyFragment = HabitModifyFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame2, habitModifyFragment)
            .commit()
    }
}