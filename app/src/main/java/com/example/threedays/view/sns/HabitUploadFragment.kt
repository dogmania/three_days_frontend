package com.example.threedays.view.sns

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.*
import com.example.threedays.databinding.FragmentHabitUploadBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        val app = activity?.application as GlobalApplication
        val mainActivity = requireActivity() as MainActivity

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = app.apiService.getUserFeed(mainActivity.email)
                Log.e("response", response.toString())

                withContext(Dispatchers.Main) {
                    postAdapter = PostAdapter(response, requireContext())
                    binding.recyclerView.adapter = postAdapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            } catch (e:Exception) {
                Log.e("HabitUploadFragment", "Error during getUserFeed API call", e)
            }
        }

        binding.btnSearch.setOnClickListener {
            val fragment = SearchFragment()
            mainActivity?.replaceFragment(fragment, "searchFragment")
        }
    }
}