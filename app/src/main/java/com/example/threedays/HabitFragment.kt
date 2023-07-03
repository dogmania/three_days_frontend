package com.example.threedays

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.databinding.FragmentEmptyHabitBinding
import com.example.threedays.databinding.FragmentHabitBinding

class HabitFragment : Fragment() {

    private lateinit var binding: FragmentHabitBinding
    private lateinit var habitAdapter: HabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nickname = requireActivity().intent.getStringExtra("nickname") ?: ""
        val user = userManager.getUser(nickname)!!
        val habits = user.habits

        habitAdapter = HabitAdapter(habits, requireContext())
        binding.habitRecyclerView.adapter = habitAdapter

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}