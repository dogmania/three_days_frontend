package com.example.threedays

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.threedays.databinding.ActivityMainBinding
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.threedays.databinding.ActivityFirstPageBinding
import com.example.threedays.view.home.HomeFragment
import com.example.threedays.view.sns.SnsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var habits : MutableList<Habit>

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
            setHabitFragment(nickname)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddHabitFirstActivity::class.java)
            intent.putExtra("nickname", nickname)
            startActivity(intent)
            finish()
        }

        binding.btnHome.setOnClickListener {
            setHabitFragment(nickname)
        }

        binding.btnProfile.setOnClickListener {
            setProfileFragment(nickname)
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
            setHabitFragment(nickname)
        }
    }

    private fun setEmptyFragment(nickname: String) {
        val fragment = EmptyHabitFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        replaceFragment(fragment)
    }

    private fun setHabitFragment(nickname: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.sub_frame)

        if (currentFragment is HabitFragment) {
            return
        }

        val fragment = HabitFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        replaceFragment(fragment)
    }

    private fun setProfileFragment(nickname: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.sub_frame)

//        if (currentFragment is ProfileFragment) {
//            return
//        }
    }

    fun showModifyFragment(nickname: String) {
        binding.navigationLayout.visibility = View.GONE

        val habitModifyFragment = HabitModifyFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }
        replaceFragment(habitModifyFragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.sub_frame, fragment)
            .addToBackStack(null) // 이전 프래그먼트를 백스택에 추가
            .commit()
    }

    override fun onBackPressed() {
        binding.navigationLayout.visibility = View.VISIBLE

        if (supportFragmentManager.backStackEntryCount > 0) {
            // 스택에 프래그먼트가 남아있을 경우, 이전 프래그먼트로 돌아갑니다.
            supportFragmentManager.popBackStack()
        } else {
            // 스택에 프래그먼트가 없는 경우, 기본 뒤로가기 동작을 수행합니다.
            super.onBackPressed()
        }
    }
}