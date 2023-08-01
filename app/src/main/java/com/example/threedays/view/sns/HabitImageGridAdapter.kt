package com.example.threedays.view.sns

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.threedays.api.Certification
import com.example.threedays.databinding.ItemHabitImageBinding

class HabitImageGridAdapter(private val certification: List<Certification>): RecyclerView.Adapter<HabitImageGridAdapter.GridViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, certification: List<Certification>)
    }

    inner class GridViewHolder(binding: ItemHabitImageBinding): RecyclerView.ViewHolder(binding.root) {
        val habitImage = binding.habitImage

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding = ItemHabitImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val certificationData = certification[position]
        val firstUrl = certificationData.imagUrls[0]

        Glide.with(holder.itemView.context)
            .load(firstUrl)
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // 디스크 캐시 사용 설정
            .into(holder.habitImage)

        holder.root.setOnClickListener {
            onItemClickListener?.onItemClick(holder.adapterPosition, certification)
        }
    }

    override fun getItemCount(): Int {
        return certification.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}