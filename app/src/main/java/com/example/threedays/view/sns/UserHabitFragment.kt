package com.example.threedays.view.sns

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.GlobalApplication
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.api.Certification
import com.example.threedays.databinding.FragmentUserHabitBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserHabitFragment : Fragment() {
    private lateinit var binding: FragmentUserHabitBinding
    private lateinit var nickname : String
    private lateinit var habitName: String
    private var id = -1L
    private lateinit var createdDate: String
    private lateinit var certification: List<Certification>
    private lateinit var adapter: UserHabitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserHabitBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments?.getLong("userId", -1)!!
        nickname = arguments?.getString("nickname")!!
        habitName = arguments?.getString("habitName")!!

        val app = activity?.application as GlobalApplication
        val mainActivity = requireActivity() as MainActivity

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = app.apiService.getUserProfile(id, mainActivity.email)

                synchronized(this@UserHabitFragment) {
                    val habits = response.habitList
                    val habit = habits.find { it.title == habitName }!!
                    certification = habit.certifyDtos
                    createdDate = habit.createdHabit
                }

                withContext(Dispatchers.Main) {
                    binding.habitName.text = habitName
                    adapter = UserHabitAdapter(certification, nickname, habitName, requireContext(), createdDate, response.kakaoImageUrl)
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }

            } catch (e: Exception) {
                Log.e("UserHabitFragment", "Error during getUserProfile API call", e)
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}