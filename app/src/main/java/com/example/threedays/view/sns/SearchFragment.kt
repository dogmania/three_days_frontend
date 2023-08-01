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
import com.example.threedays.databinding.FragmentSearchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var habitTitle: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            setRecyclerView()
        }
    }

    private fun setRecyclerView() {
        val app = activity?.application as GlobalApplication

        habitTitle = binding.habitTitle.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = app.apiService.searchUser(habitTitle)

                withContext(Dispatchers.Main) {
                    searchAdapter = SearchAdapter(response, habitTitle)
                    binding.recyclerView.adapter = searchAdapter
                    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

                    searchAdapter.setOnItemClickListener(object: SearchAdapter.OnItemClickListener {
                        override fun onItemClick(id: Long) {
                            val userFragment = UserFragment()
                            val args = Bundle()
                            args.putLong("userId", id)
                            userFragment.arguments = args

                            val activity = requireActivity() as MainActivity
                            activity.replaceFragment(userFragment, "userFragment")
                        }
                    })
                }
            } catch (e: Exception) {
                Log.e("SearchFragment", "Error during searchUser API call", e)
            }
        }
    }
}