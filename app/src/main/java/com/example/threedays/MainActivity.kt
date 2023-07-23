package com.example.threedays

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.View
import com.example.threedays.databinding.ActivityMainBinding
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.threedays.databinding.ActivityFirstPageBinding
import com.example.threedays.view.home.HomeFragment
import com.example.threedays.view.sns.HabitUploadFragment
import com.example.threedays.view.sns.MyHabitFragment
import com.example.threedays.view.sns.ProfileFragment
import com.example.threedays.view.sns.SnsFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.threedays.api.Habit

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var habits : MutableList<Habit>
    private lateinit var app: GlobalApplication
    private lateinit var email: String
    private lateinit var nickname: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = applicationContext as GlobalApplication
        sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)

        nickname = app.nickname
        email = sharedPreferences.getString("email", null)!!

        CoroutineScope(Dispatchers.IO).launch {
            try {
                habits = app.apiService.getHabits(email)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during getHabits API call: ${e.message}", e)
            }

            if (habits.isNullOrEmpty()) {
                setEmptyFragment(nickname)//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정
            } else {
                setHabitFragment(nickname)
            }
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddHabitFirstActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnHome.setOnClickListener {
            setHabitFragment(nickname)
        }

        binding.btnProfile.setOnClickListener {
            setProfileFragment(nickname)
        }

        binding.btnThumbsUp.setOnClickListener {
            setHabitUploadFragment(nickname)
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                habits = app.apiService.getHabits(email)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during getHabits API call: ${e.message}", e)
            }

            if (habits.isNullOrEmpty()) {
                setEmptyFragment(nickname)//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정
            } else {
                setHabitFragment(nickname)
            }
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

        if (currentFragment is ProfileFragment) {
            return
        }

        val fragment = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        replaceFragment(fragment)
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

    private fun setHabitUploadFragment(nickname: String) {
        val habitUploadFragment = HabitUploadFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        replaceFragment(habitUploadFragment)
    }

    fun replaceFragment(fragment: Fragment) {
        if (fragment is HabitFragment) {
            binding.title.visibility = View.VISIBLE
            binding.btnAdd.visibility = View.VISIBLE
        } else if (fragment is ProfileFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
        } else if (fragment is HabitUploadFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
        } else if (fragment is MyHabitFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
        }

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

    fun getHabits(): MutableList<com.example.threedays.api.Habit> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                habits = app.apiService.getHabits(email)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during getHabits API call: ${e.message}", e)
            }
        }
        return habits
    }
}