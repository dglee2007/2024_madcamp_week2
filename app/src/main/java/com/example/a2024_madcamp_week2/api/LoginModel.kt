package com.example.a2024_madcamp_week2.api

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("name") val name: String,
    @SerializedName("access_token") val accessToken: String,
)

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("user_id")
    val userId: Int,
    val name: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("created_at")
    val createdAt: String
)

