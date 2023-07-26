package com.example.threedays.view.edit

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.threedays.AddHabitThirdActivity
import com.example.threedays.HabitFragment
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.databinding.FragmentEditTitleBinding

class EditTitleFragment : Fragment() {
    private lateinit var binding: FragmentEditTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTitleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val duration = arguments?.getInt("duration")!!
        val id = arguments?.getLong("id")!!
        val mainActivity = requireActivity() as MainActivity

        binding.btnBack.setOnClickListener {
            mainActivity.closeFragment()
        }

        binding.btnClose.setOnClickListener {
            val fragment = HabitFragment()

            mainActivity.replaceFragment(fragment, "habitFragment")
        }

        binding.btnComplete.setOnClickListener {
            checkEditText(duration, id)
        }
    }

    private fun checkEditText(duration : Int, id: Long) {
        val habitName = binding.setHabitEdittext.text.toString()

        if (habitName.isBlank()) {
            Toast.makeText(requireContext(), "습관을 설정해주세요",
                Toast.LENGTH_SHORT).show()
        } else {
            val mainActivity = requireActivity() as MainActivity
            val fragment = EditVisibleFragment().apply{
                arguments = Bundle().apply {
                    putString("habitName", habitName)
                    putInt("duration", duration)
                    putLong("id", id)
                }
            }
            mainActivity.replaceFragmentMain(fragment, "editVisibleFragment")
        }
    }
}