package com.example.threedays.view.sns

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.threedays.R
import com.example.threedays.databinding.SnsItemBinding

class SnsAdapter(private val dataList: ArrayList<SnsData>): RecyclerView.Adapter<SnsAdapter.DataViewHolder>() {

    inner class DataViewHolder(private val binding: SnsItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: SnsData){
            binding.tvSnsDDay.text = data.d_day
         //   viewBinding.snsRatingBar.rating = 3
            binding.tvSnsTitle.text = data.sns_title
            binding.tvSnsContent.text = data.sns_content

            binding.snsViewpager.adapter = SnsViewPagerAdapter(getImageList())
            binding.snsViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            //indicator 설정
            val indicator = binding.snsIndicator
            indicator.setViewPager(binding.snsViewpager)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val viewBinding = SnsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataList[position])

    }

    override fun getItemCount(): Int = dataList.size

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    private fun getImageList(): ArrayList<Int> {
        return arrayListOf<Int>(
            R.drawable.note1,
            R.drawable.note2
        )
    }

}