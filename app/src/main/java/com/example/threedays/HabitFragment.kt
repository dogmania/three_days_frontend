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

        val mainActivity = requireActivity() as? MainActivity
        val nickname = mainActivity?.nickname!!
        val habits = mainActivity?.getHabits()!!

        habitAdapter = HabitAdapter(habits, requireContext(), nickname)
        binding.habitRecyclerView.adapter = habitAdapter

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnModify.setOnClickListener {
            val mainActivity = requireActivity() as? MainActivity
            mainActivity?.showModifyFragment(nickname)
        }
    }

    override fun onResume() {
        super.onResume()

        val mainActivity = requireActivity() as? MainActivity
        val nickname = mainActivity?.nickname!!
        val habits = mainActivity?.getHabits()!!

        habitAdapter = HabitAdapter(habits, requireContext(), nickname)
        binding.habitRecyclerView.adapter = habitAdapter

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    fun updateAdapter() {
        habitAdapter.notifyDataSetChanged()
    }
}