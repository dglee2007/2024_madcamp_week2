package com.example.a2024_madcamp_week2.api

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("image") val image: String,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("name") val name: String
)

