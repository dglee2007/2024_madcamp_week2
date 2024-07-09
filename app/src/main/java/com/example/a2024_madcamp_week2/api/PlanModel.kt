package com.example.a2024_madcamp_week2.api

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PlanRequest(
    @SerializedName("concert_id") val concertId: Int,
    @SerializedName("date") val date: String
)

data class PlanResponse(
    @SerializedName("date") val date: String,
    @SerializedName("concert") val concert: ConcertResponse
)

data class ConcertResponse(
    @SerializedName("title") val title: String,
    @SerializedName("place") val place: String,
    @SerializedName("date") val date: String,
    @SerializedName("image_url") val imageUrl: String
)