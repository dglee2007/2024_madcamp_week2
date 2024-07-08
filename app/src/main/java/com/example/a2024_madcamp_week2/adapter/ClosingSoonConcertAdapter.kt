package com.example.a2024_madcamp_week2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.ClosingSoonConcertResponse
import com.example.a2024_madcamp_week2.databinding.ItemClosingSoonConcertBinding

class ClosingSoonConcertAdapter(private val closingSoonConcerts: List<ClosingSoonConcertResponse>)
    : RecyclerView.Adapter<ClosingSoonConcertAdapter.ClosingSoonConcertViewHolder>() {

    // 보여줄 아이템 개수 설정
    private val itemCountToShow = 5

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosingSoonConcertViewHolder {
        return ClosingSoonConcertViewHolder(
            ItemClosingSoonConcertBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ClosingSoonConcertViewHolder, position: Int) {
        if (position < itemCountToShow) { // position이 itemCountToShow 보다 작을 때만 데이터 바인딩
            val closingSoonConcert = closingSoonConcerts[position]
            holder.bind(closingSoonConcert)
        } else {
            holder.clear() // 나머지 아이템은 빈 상태로 처리
        }
    }

    override fun getItemCount(): Int {
        // 실제 아이템 개수가 itemCountToShow보다 작을 경우 실제 개수 반환
        return minOf(closingSoonConcerts.size, itemCountToShow)
    }

    class ClosingSoonConcertViewHolder(private val binding: ItemClosingSoonConcertBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(closingSoonConcert: ClosingSoonConcertResponse) {
            binding.titleTextView.text = closingSoonConcert.title
            binding.placeTextView.text = closingSoonConcert.place
            binding.dateTextView.text = closingSoonConcert.date

            Glide.with(binding.imageView.context)
                .load(closingSoonConcert.imageUrl) // closingSoonConcert.imageUrl는 String 형태의 이미지 URL
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