package com.example.threedays.view.sns

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.GlobalApplication
import com.example.threedays.HabitCertification
import com.example.threedays.MainActivity
import com.example.threedays.api.Certification
import com.example.threedays.databinding.FragmentMyHabitBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyHabitFragment : Fragment() {
    private lateinit var binding: FragmentMyHabitBinding
    private lateinit var nickname : String
    private lateinit var habitName: String
    private lateinit var createdDate: String
    private lateinit var certification: List<Certification>
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

        nickname = arguments?.getString("nickname")!!
        habitName = arguments?.getString("habitName")!!
        val app = activity?.application as GlobalApplication
        val mainActivity = requireActivity() as MainActivity

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = app.apiService.getMyProfileFeed(mainActivity.email)

                synchronized(this@MyHabitFragment) {
                    val habits = response.habitList
                    val habit = habits.find { it.title == habitName }!!
                    certification = habit.certifyDtos
                    createdDate = habit.createdHabit
                }

                withContext(Dispatchers.Main) {
                    binding.habitName.text = habitName
                    adapter = MyHabitAdapter(certification, nickname, habitName, requireContext(), createdDate, response.kakaoImageUrl)
                    binding.recyclerView.adapter = adapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }

            } catch (e: Exception) {
                Log.e("MyHabitFragment", "Error during getMyProfileFeed API call", e)
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}