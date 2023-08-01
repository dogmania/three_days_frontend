package com.example.threedays.view.sns

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.threedays.*
import com.example.threedays.api.Certification
import com.example.threedays.api.CertifiedHabit
import com.example.threedays.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: HabitImageGridAdapter
    private lateinit var nickname: String
    private lateinit var habitName: String
    private lateinit var habits: List<CertifiedHabit>
    private lateinit var kakaoImageUrl: String
    private lateinit var keywords: List<String>
    private lateinit var certification: List<Certification>
    private var totalAchievementRate = 0
    private var totalHabitCount = 0
    private var followerCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = requireActivity() as MainActivity
        val app = activity?.application as GlobalApplication
        var index = 0
        val textViewIds = arrayOf(
            binding.firstKeywordText,
            binding.secondKeywordText,
            binding.thirdKeywordText,
            binding.fourthKeywordText,
            binding.fifthKeywordText
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = app.apiService.getMyProfileFeed(mainActivity.email)

                Log.d("ProfileFragment", "Response: $response")

                synchronized(this@ProfileFragment) {

                    habits = response.habitList
                    followerCount = response.followerCount
                    kakaoImageUrl = response.kakaoImageUrl
                    keywords = response.keywords
                    nickname = response.nickname
                    totalAchievementRate = response.totalAchievementRate
                    totalHabitCount = response.totalHabitCount
                    certification = habits[index].certifyDtos

                    Log.d("ProfileFragment", "CertifyDtos: ${certification}")
                }

                withContext(Dispatchers.Main) {

                    if (!habits.isEmpty()) {
                        binding.habitName.text= habits[0].title
                    }

                    setRecyclerView(certification, habits[0].title)

                    binding.nickname.text = nickname
                    binding.achievingCount.text = totalHabitCount.toString()
                    binding.achievingRate.text = totalAchievementRate.toString()
                    binding.followersCount.text = followerCount.toString()

                    for (i in keywords.indices) {
                        if (i < textViewIds.size) {
                            val textView = textViewIds[i]
                            textView.text = keywords[i]
                            textView.visibility = View.VISIBLE
                        }
                    }

                    if (certification != null) {
                        adapter = HabitImageGridAdapter(certification)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

                        adapter.setOnItemClickListener(object : HabitImageGridAdapter.OnItemClickListener {
                            override fun onItemClick(position: Int, certification: List<Certification>) {
                                // MyHabitFragment로 전환하면서 클릭된 아이템의 데이터 전달
                                val fragment = MyHabitFragment().apply {
                                    arguments = Bundle().apply {
                                        putString("nickname", nickname)
                                        putString("habitName", habits[0].title)
                                    }
                                }
                                val activity = requireActivity() as MainActivity
                                activity.replaceFragment(fragment, "myHabitFragment")
                            }
                        })
                    }

                    Glide.with(requireContext())
                        .load(kakaoImageUrl)
                        .apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(binding.profileImage)
                }
            } catch (e:Exception) {
                Log.e("ProfileFragment", "Error during getMyProfileFeed API call", e)
            }
        }

        binding.btnRight.setOnClickListener {
            index++
            if (index > habits.size - 1) {
                index--
            } else {
                certification = habits[index].certifyDtos
                habitName = habits[index].title
                binding.habitName.text = habitName
                setRecyclerView(certification, habitName)
            }
        }

        binding.btnLeft.setOnClickListener {
            index--
            if (index < 0) {
                index++
            } else {
                certification = habits[index].certifyDtos
                habitName = habits[index].title
                binding.habitName.text = habitName
                setRecyclerView(certification, habitName)
            }
        }

        binding.btnFollowing.visibility = View.GONE
    }

    private fun setRecyclerView(certification: List<Certification>, habitName: String) {
        val certification = certification
        val currentHabitName = habitName

        if (certification != null) {
            adapter = HabitImageGridAdapter(certification)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

            adapter.setOnItemClickListener(object : HabitImageGridAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, certification: List<Certification>) {
                    // MyHabitFragment로 전환하면서 클릭된 아이템의 데이터 전달
                    val fragment = MyHabitFragment().apply {
                        arguments = Bundle().apply {
                            putString("nickname", nickname)
                            putString("habitName", currentHabitName)
                        }
                    }
                    val activity = requireActivity() as MainActivity
                    activity.replaceFragment(fragment, "myHabitFragment")
                }
            })
        }
    }
}