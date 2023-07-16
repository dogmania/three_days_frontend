package com.example.threedays.view.sns

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.HabitAdapter
import com.example.threedays.R
import com.example.threedays.databinding.FragmentHabitUploadBinding
import com.example.threedays.userManager

class HabitUploadFragment : Fragment() {
    private lateinit var binding: FragmentHabitUploadBinding
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHabitUploadBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nickname = requireActivity().intent.getStringExtra("nickname") ?: ""
        val user = userManager.getUser(nickname)!!
        val certification = user.certification

        postAdapter = PostAdapter(certification, nickname)
        binding.recyclerView.adapter = postAdapter

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}