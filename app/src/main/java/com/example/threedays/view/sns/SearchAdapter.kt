package com.example.threedays.view.sns

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.threedays.api.SearchedUser
import com.example.threedays.databinding.ItemSearchBinding

class SearchAdapter(private val userList: List<SearchedUser>, private val searchedTitle: String)
    : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(id: Long)
    }

    inner class SearchViewHolder(binding: ItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
        val profileImage = binding.profileImage
        val name = binding.name
        val habitTitle = binding.habitTitle

        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val searchData = userList[position]

        holder.name.text = searchData.nickname
        holder.habitTitle.text = highlightKeyword(searchData.title, searchedTitle)
        Glide.with(holder.itemView.context)
            .load(searchData.kakaoProfileUrl)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(holder.profileImage)

        holder.root.setOnClickListener {
            onItemClickListener?.onItemClick(searchData.userId)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun highlightKeyword(text: String, keyword: String): SpannableStringBuilder {
        val spannableBuilder = SpannableStringBuilder(text)

        val startIndex = text.indexOf(keyword, ignoreCase = true)
        if (startIndex != -1) {
            val endIndex = startIndex + keyword.length

            // 검색한 단어의 색상을 변경할 스팬을 생성
            val colorSpan = ForegroundColorSpan(Color.BLACK)

            // 스팬을 적용하여 검색한 단어만 색상을 변경
            spannableBuilder.setSpan(
                colorSpan,
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannableBuilder
    }
}