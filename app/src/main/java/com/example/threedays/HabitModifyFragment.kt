package com.example.threedays

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.api.Habit
import com.example.threedays.databinding.ActivityMainBinding
import com.example.threedays.databinding.CustomDialogLayoutBinding
import com.example.threedays.databinding.FragmentHabitBinding
import com.example.threedays.databinding.FragmentHabitModifyBinding
import com.example.threedays.view.edit.EditDurationFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface HabitUpdateListener {
    fun onHabitUpdated(habit: com.example.threedays.api.Habit)
    fun showDeleteConfirmationDialog(checkedHabits: List<com.example.threedays.api.Habit>)
    fun onDeleteConfirmed(checkedHabits: List<com.example.threedays.api.Habit>)
    fun onHabitSelected(selectedHabits: List<com.example.threedays.api.Habit>)
    fun onModifyButtonClick(habit: com.example.threedays.api.Habit)
}

class HabitModifyFragment : Fragment(), HabitUpdateListener {
    private lateinit var binding: FragmentHabitModifyBinding
    private lateinit var habitModificationAdapter: HabitModificationAdapter
    private lateinit var habits: MutableList<com.example.threedays.api.Habit>

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

        val mainActivity = requireActivity() as? MainActivity
        habits = mainActivity?.getHabit()!!

        habitModificationAdapter = HabitModificationAdapter(habits, this)
        binding.habitRecyclerView.adapter = habitModificationAdapter

        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnDelete.setOnClickListener {
            val selectedHabits = habitModificationAdapter.getSelectedHabits()
            onHabitSelected(selectedHabits)
        }

        binding.btnStop.setOnClickListener {
            val selectedHabits = habitModificationAdapter.getSelectedHabits()
            onHabitSelected(selectedHabits)
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 습관 목록 프래그먼트로 전환
                val habitFragment = HabitFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.sub_frame, habitFragment, "habitFragment")
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onHabitUpdated(habit: com.example.threedays.api.Habit) {
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit
            habitModificationAdapter.notifyItemChanged(index)
        }
    }

    override fun showDeleteConfirmationDialog(checkedHabits: List<com.example.threedays.api.Habit>) {
        val dialogBinding = CustomDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.dialogButtonConfirm.setOnClickListener {
            dialog.dismiss()
            onDeleteConfirmed(checkedHabits)
        }

        dialogBinding.dialogButtonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDeleteConfirmed(checkedHabits: List<com.example.threedays.api.Habit>) {
        val app = activity?.application as GlobalApplication
        val mainActivity = activity as MainActivity

        for (habit in checkedHabits) {
            println("삭제 요청: ${habit.title}")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    app.apiService.deleteHabit(habit.id)
                    val updatedHabits = app.apiService.getHabits(mainActivity.email)

                    withContext(Dispatchers.Main) {
                        habits.clear()
                        habits.addAll(updatedHabits)
                        mainActivity.habits.clear()
                        mainActivity.habits.addAll(updatedHabits)
                        habitModificationAdapter.notifyDataSetChanged()
                        mainActivity.updateHabitAdapter()
                    }
                } catch (e: Exception) {
                    Log.e("HabitModifyFragment", "Error during deleteHabit API call", e)
                }
            }
        }
    }

    override fun onHabitSelected(selectedHabits: List<com.example.threedays.api.Habit>) {
        if (selectedHabits.isEmpty()) {
            Toast.makeText(requireContext(), "삭제할 습관을 선택해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            showDeleteConfirmationDialog(selectedHabits)
        }
    }

    override fun onModifyButtonClick(habit: Habit) {
        val mainActivity = requireActivity() as MainActivity
        val fragment = EditDurationFragment().apply {
            arguments = Bundle().apply {
                putLong("id", habit.id)
            }
        }
        mainActivity.closeFragment()
        mainActivity.replaceFragmentMain(fragment, "editDurationFragment")
    }
}