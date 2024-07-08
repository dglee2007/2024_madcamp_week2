package com.example.a2024_madcamp_week2.ui.dashboard

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2024_madcamp_week2.ChatRoomCreateActivity
import com.example.a2024_madcamp_week2.ReviewCreateActivity
import com.example.a2024_madcamp_week2.adapter.ChatRoomAdapter
import com.example.a2024_madcamp_week2.adapter.ReviewAdapter
import com.example.a2024_madcamp_week2.api.ApiClient
import com.example.a2024_madcamp_week2.api.ChatRoomResponse
import com.example.a2024_madcamp_week2.api.ChatService
import com.example.a2024_madcamp_week2.api.ReviewResponse
import com.example.a2024_madcamp_week2.api.ReviewService
import com.example.a2024_madcamp_week2.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var chatRoomList: ArrayList<ChatRoomResponse>
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        chatRoomList = ArrayList() // reviewList 초기화

//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.btnCreateChatRoom.setOnClickListener {
            val intent = Intent(requireContext(), ChatRoomCreateActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            getAllChatRooms()
            setupRecyclerView() // getAllReviews가 완료된 후에 RecyclerView 설정
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getAllChatRooms() {
        withContext(Dispatchers.IO) {
            // var email=binding.etSignupEmailleft.text.toString()+binding.etSignupEmailright.text.toString()
            val response = ChatService.retrofitGetAllChatRooms().execute()
            if (response.isSuccessful) {
                val chatRooms = response.body()
                if (chatRooms != null) {
                    for (chatRoom in chatRooms) {
                        val chatRoomId = chatRoom.chatRoomId
                        val title = chatRoom.title
                        val content = chatRoom.content
                        val count = chatRoom.count
                        val createdAt = chatRoom.createdAt
                        val name = chatRoom.name
                        chatRoomList.add(ChatRoomResponse(chatRoomId = chatRoomId, title=title, content=content, count = count, createdAt=createdAt, name=name))
                    }
                } else {
                    val body = response.errorBody()?.string()
                    Log.e(ContentValues.TAG, "body : $body")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        chatRoomAdapter = ChatRoomAdapter(chatRoomList)
        Log.d("chatRoomList", chatRoomList.toString())
        // reviewAdapter.setOnItemClickListener(object : ReportImageRVAdapter.OnItemClickListener {
        //    override fun onItemClick(pos: Int) {
        //    }
        // })

        val chatRoomLinearLayoutManager = LinearLayoutManager(requireContext())
        chatRoomLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChatRoom.layoutManager = chatRoomLinearLayoutManager
        binding.rvChatRoom.adapter = chatRoomAdapter
    }
}