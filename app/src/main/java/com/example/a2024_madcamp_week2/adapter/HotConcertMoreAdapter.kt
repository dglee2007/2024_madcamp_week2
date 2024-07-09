package com.example.a2024_madcamp_week2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.HotConcertResponse
import com.example.a2024_madcamp_week2.databinding.ItemHotConcertMoreBinding

class HotConcertMoreAdapter(
    private val hotConcertList: List<HotConcertResponse>,
) : RecyclerView.Adapter<HotConcertMoreAdapter.HotConcertMoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotConcertMoreViewHolder {
        return HotConcertMoreAdapter.HotConcertMoreViewHolder(
            ItemHotConcertMoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HotConcertMoreViewHolder, position: Int) {
        val hotConcert = hotConcertList[position]
        holder.bind(hotConcert)
    }

    override fun getItemCount(): Int {
        return hotConcertList.size
    }

    class HotConcertMoreViewHolder(private val binding: ItemHotConcertMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotConcert: HotConcertResponse) {
            binding.concertTitleTextView.text = hotConcert.title
            binding.concertPlaceTextView.text = hotConcert.place
            binding.concertDateTextView.text = hotConcert.date

            Glide.with(binding.concertImageView.context)
                .load(hotConcert.imageUrl) // hotConcert.imageUrl는 String 형태의 이미지 URL
                .into(binding.concertImageView)
        }
    }
}