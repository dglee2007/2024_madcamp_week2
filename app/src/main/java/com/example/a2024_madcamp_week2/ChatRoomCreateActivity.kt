package com.example.a2024_madcamp_week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.a2024_madcamp_week2.api.ApiClient
import com.example.a2024_madcamp_week2.api.ChatRoomRequest
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

        val chatRoomRequest = ChatRoomRequest(
            title = binding.editTextChatRoomTitle.text.toString(),
            content = binding.editTextChatRoomContent.text.toString(),
            count = 1
        )

        // 네트워크 요청 및 응답 처리
        val call = apiService.postChatRoom(
            chatRoomRequest = chatRoomRequest,
            userId = getUserIdFromSharedPreferences(applicationContext)
        )
        call.enqueue(object : Callback<ChatRoomResponse> {
            override fun onResponse(call: Call<ChatRoomResponse>, response: Response<ChatRoomResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Log.d("채팅방 등록 성공: ", responseData.toString())
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("채팅방 등록 실패: ", errorBody.toString())
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
            override fun onFailure(call: Call<ChatRoomResponse>, t: Throwable) {
                Log.e("TAG", "실패원인: $t")
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
    }
}