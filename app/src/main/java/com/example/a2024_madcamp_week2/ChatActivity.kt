package com.example.a2024_madcamp_week2

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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

    var btn_conn: Button? = null
    var btn_send: Button? = null
    var tv_text_con: TextView? = null
    var tv_text_from_server: TextView? = null
    var tv_text_from_server2: TextView? = null

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        btn_conn = binding.btnCon
        btn_send = binding.btnSend
        tv_text_con = binding.tvTextConn //서버로부터 온 클라이언트 id출력
        tv_text_from_server = binding.tvTextFromServer //서버로부터 온 msg출력
        tv_text_from_server2 = binding.tvTextFromServer2 //서버로부터 온 msg2출력
        btn_conn!!.setOnClickListener(
            View.OnClickListener
            //연결버튼 클릭
            {
                connect() //연결함수
            })
        btn_send!!.setOnClickListener(
            View.OnClickListener
            //보내기버튼 클릭
            {
                send() //보내기함수
            })

        setContentView(binding.root)
    }

    fun connect() {
        try {
            mSocket = IO.socket("http://143.248.226.14:3000") //안드로이드 avd사용시 로컬 호스트는 이 주소사용
            mSocket!!.connect() //위 주소로 연결
            Log.d("SOCKET", "Connection success : " + mSocket?.id())
            mSocket?.on("check_con", Emitter.Listener { args ->

                //서버가 'check_con'이벤트를 일으킨경우
                //args에 서버가 보내온 json데이터 들어간다
                runOnUiThread {
                    try {
                        val data = args[0] as JSONObject
                        tv_text_con!!.text =
                            data.getString("id") //json에서 id라는 키의 값만 빼서 텍스트뷰에 출력
                    } catch (e: Exception) {
                        Toast.makeText(
                            applicationContext, e.message,
                            Toast.LENGTH_LONG
                        ).show()
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: Exception) {
            tv_text_con!!.text = "안됨"
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG)
                .show()
            Log.e("에러", e.printStackTrace().toString())
        }
    }

    fun send() {
        if (mSocket != null) { //연결된경우에만 보내기 가능
            val data = JSONObject() //서버에게 줄 데이터를 json으로 만든다
            try {
                data.put("id", "test2525") //위에서 만든 json에 키와 값을 넣음
                data.put("password", "testpass")
                mSocket!!.emit("msg", data) //서버에게 msg 이벤트 일어나게 함

                mSocket!!.on("msg_to_client") { args ->

                    //서버가 msg_to_client이벤트 일으키면 실행
                    //args에 서버가 보낸 데이터 들어감
                    runOnUiThread {
                        try {
                            val data = args[0] as JSONObject
                            tv_text_from_server!!.text =
                                data.getString("msg") //서버가 보낸 json에서 msg라는 키의 값만 텍스트뷰에 출력
                            tv_text_from_server2!!.text =
                                data.getString("msg2") //서버가 보낸 json에서 msg2라는 키의 값만 텍스트뷰에 출력
                        } catch (e: Exception) {
                            Toast.makeText(
                                applicationContext, e.message,
                                Toast.LENGTH_LONG
                            ).show()
                            e.printStackTrace()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    applicationContext, e.message, Toast
                        .LENGTH_LONG
                ).show()
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() { //어플리케이션 종료시 실행
        super.onDestroy()
        // mSocket.emit("disconnect",null);
        mSocket!!.disconnect() //소켓을 닫는다
    }
}