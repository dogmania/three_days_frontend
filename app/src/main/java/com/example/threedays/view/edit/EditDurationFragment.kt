package com.example.threedays.view.edit

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import com.example.threedays.AddHabitSecondActivity
import com.example.threedays.HabitFragment
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.databinding.FragmentEditDurationBinding

class EditDurationFragment : Fragment() {
    private lateinit var binding: FragmentEditDurationBinding
    private lateinit var buttons : Array<ToggleButton>
    private var period : Int = 0
    private var movable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDurationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong("id")!!

        buttons = arrayOf(
            binding.btnDay1,
            binding.btnDay2,
            binding.btnDay3,
            binding.btnDay4,
            binding.btnDay5,
            binding.btnDay6,
            binding.btnDay7
        )

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                selectButton(buttons, i)
            }
        }

        binding.btnBack.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.closeFragment()
        }

        binding.btnClose.setOnClickListener {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.closeFragment()
        }

        binding.btnComplete.setOnClickListener {
            for (i in buttons.indices) {
                if (buttons[i].isChecked) {
                    movable = true
                }
            }

            if (movable) {
                val mainActivity = requireActivity() as MainActivity
                val fragment = EditTitleFragment().apply {
                    arguments = Bundle().apply {
                        putLong("id", id)
                        putInt("duration", period)
                    }
                }

                mainActivity.replaceFragmentMain(fragment, "editTitleFragment")
            } else {
                Toast.makeText(requireContext(), "기간을 선택해주세요.",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectButton(buttons : Array<ToggleButton>, index : Int) {
        for (i in buttons.indices) {
            buttons[i].isChecked = false
            buttons[i].setBackgroundResource(R.drawable.btn_white_background)
        }

        buttons[index].isChecked = true
        buttons[index].setBackgroundResource(R.drawable.btn_style_round_green)

        when (index) {
            0 -> period = 1
            1 -> period = 2
            2 -> period = 3
            3 -> period = 4
            4 -> period = 5
            5 -> period = 6
            6 -> period = 7
        }
    }
}