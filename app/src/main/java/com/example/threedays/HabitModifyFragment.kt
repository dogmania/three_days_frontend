package com.example.threedays

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.databinding.ActivityMainBinding
import com.example.threedays.databinding.FragmentHabitBinding
import com.example.threedays.databinding.FragmentHabitModifyBinding

class HabitModifyFragment : Fragment() {

    private lateinit var binding: FragmentHabitModifyBinding
    private lateinit var habitModificationAdapter: HabitModificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitModifyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nickname = requireActivity().intent.getStringExtra("nickname") ?: ""
        val mainActivity = requireActivity() as? MainActivity
        val habits = mainActivity?.getHabits()!!

        habitModificationAdapter = HabitModificationAdapter(habits, requireContext())
        binding.habitRecyclerView.adapter = habitModificationAdapter

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}