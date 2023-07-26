package com.example.threedays.view.edit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import com.example.threedays.GlobalApplication
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.api.EditHabit
import com.example.threedays.databinding.FragmentEditVisibleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditVisibleFragment : Fragment() {
    private lateinit var binding: FragmentEditVisibleBinding
    private lateinit var buttons : Array<ToggleButton>
    private var visible : Boolean = false
    private var movable : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditVisibleBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val duration = arguments?.getInt("duration")!!
        val id = arguments?.getLong("id")!!
        val habitName = arguments?.getString("habitName")!!

        buttons = arrayOf(binding.btnYes, binding.btnNo)

        for (i in buttons.indices) {
            buttons[i].setOnClickListener {
                selectButton(buttons, i)
            }
        }

        binding.btnComplete.setOnClickListener {
            insertHabit(id, duration, habitName)
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
            0 -> visible = true
            1 -> visible = false
        }
    }

    private fun insertHabit(id: Long, duration: Int, title: String) {
        for (i in buttons.indices) {
            if(buttons[i].isChecked) {
                movable = true
            }
        }

        if (movable) {
            CoroutineScope(Dispatchers.IO).launch {
                val app = activity?.application as GlobalApplication
                val sharedPreferences = app.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val email = sharedPreferences.getString("email", null)!!
                val mainActivity = requireActivity() as MainActivity

                try {
                    app.apiService.updateHabit(id, EditHabit(title, duration, visible))
                } catch (e: Exception) {
                    Log.e("EditVisibleFragment", "Error during updateHabit API call", e)
                }

                try {
                    val updatedHabits = app.apiService.getHabits(email)
                    mainActivity.habits.clear()
                    mainActivity.habits.addAll(updatedHabits)
                    mainActivity.updateHabitAdapter()
                } catch (e: Exception) {

                }

                withContext(Dispatchers.Main) {
                    val mainActivity = requireActivity() as MainActivity
                    val fragment = EditHabitCompleteFragment()

                    mainActivity.replaceFragmentMain(fragment, "editHabitCompleteFragment")
                }
            }
        }
    }
}