package com.example.threedays.view.sns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.threedays.R

class SnsViewPagerAdapter(private var images: List<Int>) : RecyclerView.Adapter<SnsViewPagerAdapter.Pager2ViewHolder>(){

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemImage : ImageView = itemView.findViewById(R.id.iv_sns_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_image, parent, false))
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemImage.setImageResource(images[position])
    }


    override fun getItemCount(): Int {
        return 2
    }
}
