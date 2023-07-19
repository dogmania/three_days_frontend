package com.example.threedays.view.sns

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.HabitCertification
import com.example.threedays.databinding.FragmentMyHabitBinding
import com.example.threedays.userManager

class MyHabitFragment : Fragment() {
    private lateinit var binding: FragmentMyHabitBinding
    private lateinit var nickname : String
    private lateinit var habitName: String
    private lateinit var certification: List<HabitCertification>
    private lateinit var adapter: MyHabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyHabitBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        nickname = requireActivity().intent.getStringExtra("nickname") ?: ""
//        habitName = requireActivity().intent.getStringExtra("habitName") ?: ""
        val nickname1 = arguments?.getString("nickname")
        val habitName1 = arguments?.getString("habitName")
        if (nickname1 != null && habitName1 != null) {
            val user = userManager.getUser(nickname1)!!
            val habit = user.habits.find {it.habitName == habitName1}!!
            val certification1 = habit.certification

            nickname = nickname1
            habitName = habitName1
            certification = certification1
        }

        binding.habitName.text = habitName

        adapter = MyHabitAdapter(certification, nickname, habitName)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}