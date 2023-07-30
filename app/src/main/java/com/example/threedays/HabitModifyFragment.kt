package com.example.threedays

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.api.Habit
import com.example.threedays.api.ModifyFragmentHabit
import com.example.threedays.databinding.ActivityMainBinding
import com.example.threedays.databinding.CustomDialogEditLayoutBinding
import com.example.threedays.databinding.CustomDialogLayoutBinding
import com.example.threedays.databinding.FragmentHabitBinding
import com.example.threedays.databinding.FragmentHabitModifyBinding
import com.example.threedays.view.edit.EditDurationFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface HabitUpdateListener {
    fun onHabitUpdated(habit: ModifyFragmentHabit)
    fun showDeleteConfirmationDialog(checkedHabits: List<ModifyFragmentHabit>)
    fun showEditConfirmationDialog(checkedHabits: List<ModifyFragmentHabit>)
    fun onDeleteConfirmed(checkedHabits: List<ModifyFragmentHabit>)
    fun onHabitSelected(selectedHabits: List<ModifyFragmentHabit>)
    fun onModifyButtonClick(habit: ModifyFragmentHabit)
    fun onHabitSelectedEdit(selectedHabits: List<ModifyFragmentHabit>)
    fun onEditConfirmed(checkedHabits: List<ModifyFragmentHabit>)
}

class HabitModifyFragment : Fragment(), HabitUpdateListener {
    private lateinit var binding: FragmentHabitModifyBinding
    private lateinit var habitModificationAdapter: HabitModificationAdapter
    private lateinit var habits: MutableList<ModifyFragmentHabit>

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
        habits = mainActivity?.getHabitEditList()!!

        habitModificationAdapter = HabitModificationAdapter(habits, this)
        binding.habitRecyclerView.adapter = habitModificationAdapter
        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.btnDelete.setOnClickListener {
            val selectedHabits = habitModificationAdapter.getSelectedHabits()
            onHabitSelected(selectedHabits)
        }

        binding.btnStop.setOnClickListener {
            val selectedHabits = habitModificationAdapter.getSelectedHabits()
            onHabitSelectedEdit(selectedHabits)
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

    override fun onHabitUpdated(habit: ModifyFragmentHabit) {
        val index = habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            habits[index] = habit
            habitModificationAdapter.notifyItemChanged(index)
        }
    }

    override fun showDeleteConfirmationDialog(checkedHabits: List<ModifyFragmentHabit>) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_layout)

        val dialogBinding = CustomDialogLayoutBinding.bind(dialog.findViewById(R.id.dialogLayout))

        dialogBinding.dialogButtonConfirm.setOnClickListener {
            dialog.dismiss()
            onDeleteConfirmed(checkedHabits)
        }

        dialogBinding.dialogButtonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.round_frame_white)
    }

    override fun showEditConfirmationDialog(checkedHabits: List<ModifyFragmentHabit>) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_edit_layout)

        val dialogBinding = CustomDialogEditLayoutBinding.bind(dialog.findViewById(R.id.editDialog))

        dialogBinding.dialogButtonConfirm.setOnClickListener {
            dialog.dismiss()
            onEditConfirmed(checkedHabits)
        }

        dialogBinding.dialogButtonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

        val window = dialog.window
        window?.setBackgroundDrawableResource(R.drawable.round_frame_white)
    }

    override fun onDeleteConfirmed(checkedHabits: List<ModifyFragmentHabit>) {
        val app = activity?.application as GlobalApplication
        val mainActivity = activity as MainActivity

        for (habit in checkedHabits) {
            println("삭제 요청: ${habit.title}")

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    app.apiService.deleteHabit(habit.id)
                    val updatedEditHabits = app.apiService.getHabitEditList(mainActivity.email)
                    val updatedHabits = app.apiService.getHabits(mainActivity.email)

                    withContext(Dispatchers.Main) {
                        habits.clear()
                        habits.addAll(updatedEditHabits)
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

    override fun onEditConfirmed(checkedHabits: List<ModifyFragmentHabit>) {
        val app = activity?.application as GlobalApplication
        val mainActivity = activity as MainActivity

        for (habit in checkedHabits) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    app.apiService.stopHabit(habit.id)
                    val updatedEditHabits = app.apiService.getHabitEditList(mainActivity.email)
                    val updatedHabits = app.apiService.getHabits(mainActivity.email)

                    withContext(Dispatchers.Main) {
                        habits.clear()
                        habits.addAll(updatedEditHabits)
                        mainActivity.habits.clear()
                        mainActivity.habits.addAll(updatedHabits)
                        habitModificationAdapter.notifyDataSetChanged()
                        mainActivity.updateHabitModificationAdapter()
                    }
                } catch (e:Exception) {
                    Log.e("HabitModifyFragment", "Error during stopHabit API call", e)
                }
            }
        }
    }

    override fun onHabitSelected(selectedHabits: List<ModifyFragmentHabit>) {
        if (selectedHabits.isEmpty()) {
            Toast.makeText(requireContext(), "삭제할 습관을 선택해주세요.", Toast.LENGTH_SHORT).show()
        } else {
            showDeleteConfirmationDialog(selectedHabits)
        }
    }

    override fun onHabitSelectedEdit(selectedHabits: List<ModifyFragmentHabit>) {
        if (selectedHabits.isEmpty()) {
            Toast.makeText(requireContext(), "수정할 습관을 선택해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val stopHabit = selectedHabits.find { it.stopDate?.isNotEmpty() == true }
        if (stopHabit != null) {
            Toast.makeText(requireContext(), "그만둔 습관은 체크를 해제해주세요.", Toast.LENGTH_SHORT).show()
            return // 함수를 빠져나감
        }

        showEditConfirmationDialog(selectedHabits)

    }

    override fun onModifyButtonClick(habit: ModifyFragmentHabit) {
        val mainActivity = requireActivity() as MainActivity
        val fragment = EditDurationFragment().apply {
            arguments = Bundle().apply {
                putLong("id", habit.id)
            }
        }
        mainActivity.replaceFragmentMain(fragment, "editDurationFragment")
    }

    fun updateAdapter() {
        val app = activity?.application as GlobalApplication
        val mainActivity = activity as MainActivity
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val updatedEditHabits = app.apiService.getHabitEditList(mainActivity.email)
                val updatedHabits = app.apiService.getHabits(mainActivity.email)

                withContext(Dispatchers.Main) {
                    habits.clear()
                    habits.addAll(updatedEditHabits)
                    mainActivity.habits.clear()
                    mainActivity.habits.addAll(updatedHabits)
                    habitModificationAdapter.notifyDataSetChanged()
                    mainActivity.updateHabitModificationAdapter()
                }
            } catch (e:Exception) {
                Log.e("HabitModifyFragment", "Error during stopHabit API call", e)
            }
        }

        habitModificationAdapter.notifyDataSetChanged()
        binding.habitRecyclerView.adapter = habitModificationAdapter
        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()

        val app = activity?.application as GlobalApplication
        val mainActivity = activity as MainActivity
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val updatedEditHabits = app.apiService.getHabitEditList(mainActivity.email)
                val updatedHabits = app.apiService.getHabits(mainActivity.email)

                withContext(Dispatchers.Main) {
                    habits.clear()
                    habits.addAll(updatedEditHabits)
                    mainActivity.habits.clear()
                    mainActivity.habits.addAll(updatedHabits)
                    habitModificationAdapter.notifyDataSetChanged()
                    mainActivity.updateHabitModificationAdapter()
                }
            } catch (e:Exception) {
                Log.e("HabitModifyFragment", "Error during stopHabit API call", e)
            }
        }

        habitModificationAdapter.notifyDataSetChanged()
        binding.habitRecyclerView.adapter = habitModificationAdapter
        binding.habitRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}