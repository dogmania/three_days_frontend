package com.example.threedays.view.sns

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.threedays.HabitCertification
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.databinding.FragmentProfileBinding
import com.example.threedays.userManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: HabitImageGridAdapter
    private lateinit var nickname: String
    private lateinit var habitName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nickname = requireActivity().intent.getStringExtra("nickname") ?: ""
        val user = userManager.getUser(nickname)!!
        val habits = user.habits
        var index = 0
        var certification = habits[index].certification


        if (!habits.isEmpty()) {
            binding.habitName.text= habits[0].habitName
        }

        setRecyclerView(certification, habits[0].habitName)

        binding.btnRight.setOnClickListener {
            index++
            if (index > habits.size - 1) {
                index--
            } else {
                certification = habits[index].certification
                habitName = habits[index].habitName
                binding.habitName.text = habitName
                setRecyclerView(certification, habitName)
            }
        }

        binding.btnLeft.setOnClickListener {
            index--
            if (index < 0) {
                index++
            } else {
                certification = habits[index].certification
                habitName = habits[index].habitName
                binding.habitName.text = habitName
                setRecyclerView(certification, habitName)
            }
        }
    }

    private fun setRecyclerView(certification: MutableList<HabitCertification>?, habitName: String) {
        val certification = certification
        val currentHabitName = habitName

        if (certification != null) {
            adapter = HabitImageGridAdapter(certification)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

            adapter.setOnItemClickListener(object : HabitImageGridAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, certification: List<HabitCertification>) {
                    // MyHabitFragment로 전환하면서 클릭된 아이템의 데이터 전달
                    val fragment = MyHabitFragment().apply {
                        arguments = Bundle().apply {
                            putString("nickname", nickname)
                            putString("habitName", currentHabitName)
                        }
                    }
                    val activity = requireActivity() as MainActivity
                    activity.replaceFragment(fragment)
                }
            })
        }
    }
}