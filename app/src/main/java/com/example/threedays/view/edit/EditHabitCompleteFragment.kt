package com.example.threedays.view.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.threedays.GlobalApplication
import com.example.threedays.HabitFragment
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.databinding.FragmentEditHabitCompleteBinding

class EditHabitCompleteFragment : Fragment() {
    private lateinit var binding: FragmentEditHabitCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditHabitCompleteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnComplete.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            val fragment = HabitFragment()

            mainActivity.replaceFragment(fragment, "habitFragment")
        }
    }
}