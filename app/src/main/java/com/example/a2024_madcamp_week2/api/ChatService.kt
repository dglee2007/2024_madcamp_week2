package com.example.a2024_madcamp_week2.api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChatService {
    @POST("chat/room/create/{user_id}") // Replace with your API endpoint
    fun postChatRoom(
        @Part("title") title: String,
        @Part("content") content: String,
        @Part("count") count: Int,
        @Path("user_id") userId: Int
    ): Call<ChatRoomResponse>


    @GET("chat/room/all")
    fun getAllChatRooms(
    ): Call<List<ChatRoomResponse>>

    @GET("chat/room/{user_id}")
    fun getMyChatRooms(
        @Path("userId") userId: String
    ): Call<List<ChatRoomResponse>>

    companion object {
        fun create(): ReviewService {
            return ApiClient.create(ReviewService::class.java)
        }

        fun retrofitGetAllChatRooms(): Call<List<ChatRoomResponse>> {
            return ApiClient.create(ChatService::class.java).getAllChatRooms()
        }

        fun retrofitGetMyChatRooms(userId: String): Call<List<ChatRoomResponse>> {
            return ApiClient.create(ChatService::class.java).getMyChatRooms(userId)
        }
    }
}
