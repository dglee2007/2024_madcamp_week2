package com.example.a2024_madcamp_week2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a2024_madcamp_week2.databinding.ActivityChatBinding
import com.example.a2024_madcamp_week2.databinding.ActivityMainBinding
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


class ChatActivity : AppCompatActivity() {
    private var mSocket: Socket? = null

    private lateinit var binding: ActivityChatBinding
    private lateinit var btnSend: Button
    private lateinit var etMessage: EditText
    private lateinit var llMessages: LinearLayout

    private var chatRoomId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoomId = intent.getIntExtra("chatRoomId", -1)

        btnSend = binding.btnSend
        etMessage = binding.etMessage
        llMessages = binding.llMessages

        connect()

        btnSend.setOnClickListener {
            val message = etMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                etMessage.text.clear()
            } else {
                Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun connect() {
        try {
            mSocket = IO.socket("http://143.248.226.14:3000")
            mSocket!!.connect()

            Log.d("SOCKET", "Connection success : " + mSocket?.id())
            mSocket?.emit("join_room", chatRoomId)

            // 서버에서 'joined_room' 이벤트를 받을 때 호출될 콜백 설정
            mSocket?.on("joined_room", onJoinedRoom)

            // 서버에서 'message_received' 이벤트를 받을 때 호출될 콜백 설정
            mSocket?.on("message_received", onMessageReceived)

            // 서버에서 'check_con' 이벤트를 받을 때 호출될 콜백 설정
            mSocket?.on("check_con", onCheckConnection)

        } catch (e: Exception) {
            handleException(e)
        }
    }

    private val onJoinedRoom = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val message = args[0] as String
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private val onCheckConnection = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val data = args[0] as JSONObject
                val id = data.getString("id")
                binding.tvTextConn.text = id
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private val onMessageReceived = Emitter.Listener { args ->
        runOnUiThread {
            try {
                val data = args[0] as JSONObject
                val sender = data.getString("sender")
                val message = data.getString("message")
                displayMessage(sender, message)
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }

    private fun sendMessage(message: String) {
        try {
            val data = JSONObject()
            data.put("room", chatRoomId)
            data.put("message", message)
            mSocket!!.emit("msg", data)
        } catch (e: Exception) {
            handleException(e)
        }
    }

    private fun displayMessage(sender: String, message: String) {
        val messageView = TextView(this)
        messageView.text = "$sender: $message"
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)

        // 사용자가 보낸 메시지와 다른 사람이 보낸 메시지를 구분하여 텍스트뷰 정렬
        if (sender == mSocket!!.id()) {
            // 사용자가 보낸 메시지 (왼쪽 정렬)
            //messageView.setBackgroundResource(R.drawable.bg_chat_user)
            params.marginStart = 64
            params.marginEnd = 16
            messageView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        } else {
            // 다른 사람이 보낸 메시지 (오른쪽 정렬)
           // messageView.setBackgroundResource(R.drawable.bg_chat_other)
            params.marginStart = 16
            params.marginEnd = 64
            messageView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        }

        messageView.layoutParams = params
        llMessages.addView(messageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket!!.disconnect()
    }

    private fun handleException(e: Exception) {
        Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        Log.e("Exception", e.message ?: "Unknown exception")
        e.printStackTrace()
    }
}