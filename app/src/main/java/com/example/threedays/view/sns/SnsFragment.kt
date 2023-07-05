package com.example.threedays.view.sns

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threedays.databinding.FragmentSnsBinding
import java.util.*

class SnsFragment : Fragment() {
    private lateinit var binding: FragmentSnsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSnsBinding.inflate(layoutInflater)

        var snsAdapter = SnsAdapter(loadData())
       // snsAdapter.listData = loadData()

        binding.snsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = snsAdapter
        }
        return binding.root
    }

    fun loadData(): ArrayList<SnsData>{
        val sns_list : ArrayList<SnsData> = ArrayList()

        for(no in 1..3){
            val sns_title = "예제 제목 ${no} 입니다 "
            val sns_content = "예제 내용 ${no} 입니다.예제 내용 ${no} 입니다.예제 내용 ${no} 입니다.예제 내용 ${no} 입니다.예제 내용 ${no} 입니다.예제 내용 ${no} 입니다." +
                    "예제 내용 ${no} 입니다예제 내용 ${no} 입니다예제 내용 ${no} 입니다예제 내용 ${no} 입니다예제 내용 ${no} 입니다 "
            //val date = System.currentTimeMillis()
            val date = "2023.5.10"
            var snsData = SnsData("D+01", 3, sns_title, sns_content, date)
            sns_list.add(snsData)
        }
        return sns_list
    }
}