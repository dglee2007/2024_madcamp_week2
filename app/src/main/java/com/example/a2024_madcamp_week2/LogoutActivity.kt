package com.example.a2024_madcamp_week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient

class LogoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)

        val logoutButton = findViewById<Button>(R.id.logout_button)
        val unlinkButton = findViewById<Button>(R.id.unlink_button)
        val cancelButton = findViewById<Button>(R.id.cancel_button)

        logoutButton.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("test", "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                } else {
                    Log.i("test", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
                // 로그아웃 후 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        unlinkButton.setOnClickListener {
            UserApiClient.instance.unlink { error ->
                if (error != null) {
                    Log.e("test", "연결 끊기 실패", error)
                } else {
                    Log.i("test", "연결 끊기 성공. SDK에서 토큰 삭제됨")
                }
                // 연결 끊기 후 로그인 화면으로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        cancelButton.setOnClickListener {
            // 로그아웃 취소
            finish()
        }
    }
}
