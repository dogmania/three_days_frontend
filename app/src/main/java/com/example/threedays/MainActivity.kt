package com.example.threedays

import android.content.Context
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
import com.example.threedays.view.edit.EditDurationFragment
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var habits : MutableList<Habit>
    private lateinit var app: GlobalApplication
    lateinit var email: String
    lateinit var nickname: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = applicationContext as GlobalApplication
        sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        nickname = sharedPreferences.getString("nickname", null)!!
        email = sharedPreferences.getString("email", null)!!

        CoroutineScope(Dispatchers.IO).launch {
            try {
                habits = app.apiService.getHabits(email)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during getHabits API call: ${e.message}", e)
            }

            withContext(Dispatchers.Main) {
                if (habits.isNullOrEmpty()) {
                    setEmptyFragment(nickname)//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정
                } else {
                    setHabitFragment(nickname)
                }
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

            withContext(Dispatchers.Main) {
                if (habits.isNullOrEmpty()) {
                    setEmptyFragment(nickname)//유저의 습관 목록이 비어있다면 그에 맞는 프래그먼트 설정
                } else {
                    setHabitFragment(nickname)
                }
            }
        }
    }

    private fun setEmptyFragment(nickname: String) {
        val fragment = EmptyHabitFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        replaceFragment(fragment, "emptyFragment")
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

        replaceFragment(fragment, "habitFragment")
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

        replaceFragment(fragment, "profileFragment")
    }

    fun showModifyFragment(nickname: String) {
        binding.navigationLayout.visibility = View.GONE

        val habitModifyFragment = HabitModifyFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }
        replaceFragment(habitModifyFragment, "habitModifyFragment")
    }

    private fun setHabitUploadFragment(nickname: String) {
        val habitUploadFragment = HabitUploadFragment().apply {
            arguments = Bundle().apply {
                putString("nickname", nickname)
            }
        }

        replaceFragment(habitUploadFragment, "habitUploadFragment")
    }

    fun replaceFragment(fragment: Fragment, tag: String) {
        if (fragment is HabitFragment) {
            binding.title.visibility = View.VISIBLE
            binding.btnAdd.visibility = View.VISIBLE
            binding.navigationLayout.visibility = View.VISIBLE
            binding.subFrame.visibility = View.VISIBLE
            binding.mainFrame.visibility = View.GONE
        } else if (fragment is ProfileFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
            binding.navigationLayout.visibility = View.VISIBLE
            binding.subFrame.visibility = View.VISIBLE
            binding.mainFrame.visibility = View.GONE
        } else if (fragment is HabitUploadFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
            binding.navigationLayout.visibility = View.VISIBLE
            binding.subFrame.visibility = View.VISIBLE
            binding.mainFrame.visibility = View.GONE
        } else if (fragment is MyHabitFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
            binding.navigationLayout.visibility = View.VISIBLE
            binding.subFrame.visibility = View.VISIBLE
            binding.mainFrame.visibility = View.GONE
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.sub_frame, fragment, tag)
            .addToBackStack(null) // 이전 프래그먼트를 백스택에 추가
            .commit()
    }

    fun replaceFragmentMain(fragment: Fragment, tag: String) {
        if (fragment is EditDurationFragment) {
            binding.title.visibility = View.GONE
            binding.btnAdd.visibility = View.GONE
            binding.navigationLayout.visibility = View.GONE
            binding.subFrame.visibility = View.GONE
            binding.mainFrame.visibility = View.VISIBLE
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frame, fragment, tag)
            .commit()
    }

    override fun onBackPressed() {
        binding.navigationLayout.visibility = View.VISIBLE

        if (supportFragmentManager.backStackEntryCount > 0) {
            // 스택에 프래그먼트가 남아있을 경우, 이전 프래그먼트로 돌아감
            updateHabitAdapter()
            supportFragmentManager.popBackStack()
        } else {
            // 스택에 프래그먼트가 없는 경우, 기본 뒤로가기 동작을 수행
            updateHabitAdapter()
            super.onBackPressed()
        }
    }

    fun getHabit(): MutableList<com.example.threedays.api.Habit> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                habits = app.apiService.getHabits(email)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error during getHabits API call: ${e.message}", e)
            }
        }
        return habits
    }

    fun updateHabitAdapter() {
        val habitFragment = supportFragmentManager.findFragmentByTag("habitFragment") as? HabitFragment
        habitFragment?.updateAdapter()
    }

    fun closeFragment() {
        supportFragmentManager.popBackStack()
    }
}