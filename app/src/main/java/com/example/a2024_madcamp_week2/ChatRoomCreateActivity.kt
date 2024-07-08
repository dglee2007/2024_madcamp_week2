package com.example.a2024_madcamp_week2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.a2024_madcamp_week2.api.ApiClient
import com.example.a2024_madcamp_week2.api.ChatRoomResponse
import com.example.a2024_madcamp_week2.api.ChatService
import com.example.a2024_madcamp_week2.databinding.ActivityChatRoomCreateBinding
import com.example.a2024_madcamp_week2.utility.getUserIdFromSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatRoomCreateActivity: AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomCreateBinding.inflate(layoutInflater)
        binding.buttonSubmitChatRoom.setOnClickListener {
            postChatRoomFun()
        }
        setContentView(binding.root)
    }

    private fun postChatRoomFun() {
        val apiService = ApiClient.create(ChatService::class.java)

        // 네트워크 요청 및 응답 처리
        val call = apiService.postChatRoom(
            title = binding.editTextChatRoomTitle.text.toString(),
            content = binding.editTextChatRoomContent.text.toString(),
            count = 1,
            userId = getUserIdFromSharedPreferences(applicationContext)
        )
        call.enqueue(object : Callback<ChatRoomResponse> {
            override fun onResponse(call: Call<ChatRoomResponse>, response: Response<ChatRoomResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Log.d("채팅방 등록 성공: ", responseData.toString())
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("채팅방 등록 실패: ", errorBody.toString())
                }
            }
            override fun onFailure(call: Call<ChatRoomResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: $t")
            }
        })
    }
}