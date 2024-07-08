package com.example.a2024_madcamp_week2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.HotConcertResponse
import com.example.a2024_madcamp_week2.databinding.ItemHotConcertBinding

class HotConcertAdapter(private val hotConcerts: List<HotConcertResponse>)
    : RecyclerView.Adapter<HotConcertAdapter.HotConcertViewHolder>() {

    // 보여줄 아이템 개수 설정
    private val itemCountToShow = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotConcertViewHolder {
        return HotConcertViewHolder(
            ItemHotConcertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HotConcertViewHolder, position: Int) {
        if (position < itemCountToShow) { // position이 itemCountToShow 보다 작을 때만 데이터 바인딩
            val hotConcert = hotConcerts[position]
            holder.bind(hotConcert)
        } else {
            holder.clear() // 나머지 아이템은 빈 상태로 처리
        }
    }

    override fun getItemCount(): Int {
        // 실제 아이템 개수가 itemCountToShow보다 작을 경우 실제 개수 반환
        return minOf(hotConcerts.size, itemCountToShow)
    }

    class HotConcertViewHolder(private val binding: ItemHotConcertBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotConcert: HotConcertResponse) {
            binding.titleTextView.text = hotConcert.title
            binding.placeTextView.text = hotConcert.place
            binding.dateTextView.text = hotConcert.date

            Glide.with(binding.imageView.context)
                .load(hotConcert.imageUrl) // hotConcert.imageUrl는 String 형태의 이미지 URL
                .into(binding.imageView)
        }

        // 데이터를 클리어하는 메서드 추가
        fun clear() {
            binding.titleTextView.text = ""
            binding.placeTextView.text = ""
            binding.dateTextView.text = ""
            binding.imageView.setImageDrawable(null)
        }
    }
}