package com.example.a2024_madcamp_week2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.api.ReviewResponse
import com.example.a2024_madcamp_week2.databinding.ItemReviewBinding

class ReviewAdapter(private val reviews: List<ReviewResponse>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviews.size

    class ReviewViewHolder(private val binding : ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(review: ReviewResponse){
            binding.reviewTitle.text = review.title
            binding.reviewContent.text = review.content
            binding.userName.text = review.name.toString()

            Glide.with(binding.reviewImage.context)
                .load(review.image)  // review.image는 String 형태의 이미지 URL이어야 합니다.
                .into(binding.reviewImage)
        }
    }
}