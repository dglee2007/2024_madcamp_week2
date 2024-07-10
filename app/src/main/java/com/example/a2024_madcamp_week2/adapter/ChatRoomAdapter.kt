package com.example.a2024_madcamp_week2.adapter

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.a2024_madcamp_week2.ChatActivity
import com.example.a2024_madcamp_week2.ChatRoomCreateActivity
import com.example.a2024_madcamp_week2.api.ChatRoomResponse
import com.example.a2024_madcamp_week2.databinding.ItemChatRoomBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ChatRoomAdapter(private val chatRooms: List<ChatRoomResponse>) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(
            ItemChatRoomBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRooms[position]
        holder.bind(chatRoom)
    }

    override fun getItemCount(): Int = chatRooms.size

    class ChatRoomViewHolder(private val binding : ItemChatRoomBinding) :
        RecyclerView.ViewHolder(binding.root){

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(chatRoom: ChatRoomResponse){
            binding.tvTitle.text = chatRoom.title
            binding.tvContent.text = chatRoom.content
            binding.tvCreatorName.text = chatRoom.name
            binding.tvCreatedAt.text = chatRoom.createdAt

            try {
                val parsedDate = LocalDateTime.parse(chatRoom.createdAt, DateTimeFormatter.ISO_DATE_TIME)
                val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                binding.tvCreatedAt.text = formattedDate
            } catch (e: DateTimeParseException) {
                binding.tvCreatedAt.text = chatRoom.createdAt
            }

            // 채팅방 입장 버튼 클릭 리스너 설정
            binding.btnEnterChat.setOnClickListener {
                Toast.makeText(itemView.context, "Entering chat room ${chatRoom.title}", Toast.LENGTH_SHORT).show()

                val intent = Intent(itemView.context, ChatActivity::class.java)
                intent.putExtra("chatRoomId", chatRoom.chatRoomId)
                itemView.context.startActivity(intent)
            }
        }
    }
}