package com.example.a2024_madcamp_week2.api

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class HotConcertResponse(
    @SerializedName("concert_id") val concertId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("place") val place: String,
    @SerializedName("date") val date: String,
    @SerializedName("image_url") val imageUrl: String
)

data class ClosingSoonConcertResponse(
    @SerializedName("concert_id") val concertId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("place") val place: String,
    @SerializedName("date") val date: String,
    @SerializedName("image_url") val imageUrl: String
)
