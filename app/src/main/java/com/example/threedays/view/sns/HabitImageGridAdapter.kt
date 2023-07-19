package com.example.threedays.view.sns

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.HabitCertification
import com.example.threedays.databinding.ItemHabitImageBinding

class HabitImageGridAdapter(private val certification: List<HabitCertification>): RecyclerView.Adapter<HabitImageGridAdapter.GridViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, certification: List<HabitCertification>)
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
        val firstUri = certificationData.image[0]

        holder.habitImage.setImageURI(firstUri)

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