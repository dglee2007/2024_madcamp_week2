package com.example.a2024_madcamp_week2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.ClosingSoonConcertResponse
import com.example.a2024_madcamp_week2.databinding.ItemClosingSoonConcertMoreBinding

class ClosingSoonConcertMoreAdapter(
    private val closingSoonConcertList: List<ClosingSoonConcertResponse>,
) : RecyclerView.Adapter<ClosingSoonConcertMoreAdapter.ClosingSoonConcertMoreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClosingSoonConcertMoreViewHolder {
        return ClosingSoonConcertMoreAdapter.ClosingSoonConcertMoreViewHolder(
            ItemClosingSoonConcertMoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ClosingSoonConcertMoreViewHolder, position: Int) {
        val closingSoonConcert = closingSoonConcertList[position]
        holder.bind(closingSoonConcert)
    }

    override fun getItemCount(): Int {
        return closingSoonConcertList.size
    }

    class ClosingSoonConcertMoreViewHolder(private val binding: ItemClosingSoonConcertMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(closingSoonConcert: ClosingSoonConcertResponse) {
            binding.concertTitleTextView.text = closingSoonConcert.title
            binding.concertPlaceTextView.text = closingSoonConcert.place
            binding.concertDateTextView.text = closingSoonConcert.date

            Glide.with(binding.concertImageView.context)
                .load(closingSoonConcert.imageUrl) // hotConcert.imageUrl는 String 형태의 이미지 URL
                .into(binding.concertImageView)
        }
    }
}