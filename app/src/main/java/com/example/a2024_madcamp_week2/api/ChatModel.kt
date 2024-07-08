package com.example.a2024_madcamp_week2.api

import com.google.gson.annotations.SerializedName

data class ChatRoomResponse(
    @SerializedName("chat_room_id") val chatRoomId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("count") val count: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("name") val name: String
)
