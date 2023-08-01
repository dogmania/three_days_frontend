package com.example.threedays.view.sns

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.threedays.GlobalApplication
import com.example.threedays.MainActivity
import com.example.threedays.R
import com.example.threedays.api.Certification
import com.example.threedays.api.CertifiedHabit
import com.example.threedays.databinding.FragmentUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var adapter: HabitImageGridAdapter
    private lateinit var nickname: String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var habitName: String
    private lateinit var habits: List<CertifiedHabit>
    private lateinit var kakaoImageUrl: String
    private lateinit var keywords: List<String>
    private lateinit var certification: List<Certification>
    private var totalAchievementRate = 0
    private var totalHabitCount = 0
    private var followerCount = 0
    private var isFollowing = false
    private var userId = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getLong("userId")!!
        val app = activity?.application as GlobalApplication
        sharedPreferences = app.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val id = sharedPreferences.getLong("id", 1)
        val email = sharedPreferences.getString("email", null)
        var index = 0
        val textViewIds = arrayOf(
            binding.firstKeywordText,
            binding.secondKeywordText,
            binding.thirdKeywordText,
            binding.fourthKeywordText,
            binding.fifthKeywordText
        )

        Log.d("email", email.toString())
        Log.d("userId", userId.toString())
        Log.d("id", id.toString())

        if (userId != null && email != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = app.apiService.getUserProfile(userId, email)

                    synchronized(this@UserFragment) {
                        habits = response.habitList
                        followerCount = response.followerCount
                        kakaoImageUrl = response.kakaoImageUrl
                        keywords = response.keywords
                        nickname = response.nickname
                        totalAchievementRate = response.totalAchievementRate
                        totalHabitCount = response.totalHabitCount
                        certification = habits[index].certifyDtos
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
                                    val fragment = UserHabitFragment()
                                    val args = Bundle()
                                    args.putLong("userId", userId)
                                    args.putString("nickname", nickname)
                                    args.putString("habitName", habits[0].title)
                                    fragment.arguments = args

                                    val activity = requireActivity() as MainActivity
                                    activity.replaceFragment(fragment, "userHabitFragment")
                                }
                            })
                        }

                        if (response.isFollowing) {
                            binding.btnFollowing.setBackgroundResource(R.drawable.ic_btn_following)
                            isFollowing = response.isFollowing
                        }

                        Glide.with(requireContext())
                            .load(kakaoImageUrl)
                            .apply(RequestOptions.bitmapTransform(CircleCrop()))
                            .into(binding.profileImage)
                    }
                } catch (e: Exception) {
                    Log.e("UserFragment", "Error during getUserProfile API call", e)
                }
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

        binding.btnFollowing.setOnClickListener {
            if(isFollowing) {
                binding.btnFollowing.setBackgroundResource(R.drawable.ic_btn_unfollowing)
                isFollowing = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        app.apiService.unFollow(id, userId)
                    } catch (e: Exception) {
                        Log.e("UserFragment", "Error during unFollow API call", e)
                    }
                }
            } else {
                binding.btnFollowing.setBackgroundResource(R.drawable.ic_btn_following)
                isFollowing = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        app.apiService.follow(id, userId)
                    } catch (e: Exception) {
                        Log.e("UserFragment", "Error during unFollow API call", e)
                    }
                }
            }
        }

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
                    val fragment = UserHabitFragment()
                    val args = Bundle()
                    args.putLong("userId", userId)
                    args.putString("nickname", nickname)
                    args.putString("habitName", currentHabitName)
                    fragment.arguments = args

                    val activity = requireActivity() as MainActivity
                    activity.replaceFragment(fragment, "userHabitFragment")
                }
            })
        }
    }
}