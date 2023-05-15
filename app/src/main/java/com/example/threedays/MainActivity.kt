package com.example.threedays

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.threedays.databinding.ActivityFirstPageBinding
import com.example.threedays.databinding.ActivityMainBinding
import com.example.threedays.view.home.HomeFragment
import com.example.threedays.view.sns.SnsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.bottom_nav_first->replaceFragment(HomeFragment())
                R.id.bottom_nav_second->replaceFragment(HomeFragment())
                R.id.bottom_nav_third->replaceFragment(SnsFragment())
                R.id.bottom_nav_fourth->replaceFragment(HomeFragment())

                else->{

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}