package com.example.a2024_madcamp_week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a2024_madcamp_week2.api.LoginRequest
import com.example.a2024_madcamp_week2.api.LoginResponse
import com.example.a2024_madcamp_week2.api.LoginService
import com.example.a2024_madcamp_week2.utility.saveUserIdToSharedPreferences
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit
    private lateinit var name: String
    private lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 버튼 클릭 리스너 추가
        val kakaoLoginButton: ImageButton = findViewById(R.id.btn_kakao_login)
        kakaoLoginButton.setOnClickListener {
            //카카오톡 설치되어있는지 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e("로그인", "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoTalk
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e("로그인", "로그인 성공 ${token.accessToken}")
                        makeToast(token.accessToken)
                    }
                }
            } else {
                //카카오 이메일 로그인
                UserApiClient.instance.loginWithKakaoAccount(this){ token, error ->
                    // 로그인 실패 부분
                    if (error != null) {
                        Log.e("로그인", "로그인 실패 $error")
                        // 사용자가 취소
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                            return@loginWithKakaoAccount
                        }
                        // 다른 오류
                        else {
                            UserApiClient.instance.loginWithKakaoAccount(this, callback = mCallback)
                        }
                    }
                    // 로그인 성공 부분
                    else if (token != null) {
                        Log.e("로그인", "로그인 성공 ${token.accessToken}")
                        makeToast(token.accessToken)
                    }
                }
            }
        }
    }

    fun btnKakaoLogin(view: View) {
        // 이 메서드는 XML에서 onClick 속성에 의해 호출될 수 있습니다.
        // 하지만 이미 setOnClickListener를 설정했으므로 이 메서드는 필요 없습니다.
        // 필요에 따라 이 메서드를 삭제할 수 있습니다.
    }

    private fun makeToast(accessToken: String){
        UserApiClient.instance.me { user, error ->
            Log.e("로그인", "닉네임 ${user?.kakaoAccount?.profile?.nickname}")
            Log.e("로그인", "이메일 ${user?.kakaoAccount?.email}")
            name = user?.kakaoAccount?.profile?.nickname.toString()
            Toast.makeText(
                this,
                "${user?.kakaoAccount?.profile?.nickname}님 환영합니다.",
                Toast.LENGTH_SHORT
            ).show()
            postLoginInfo(accessToken)
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private val mCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (token != null) {
            Log.e("로그인", "로그인 성공 ${token.accessToken}")
            makeToast(token.accessToken)
        }
    }

    private fun postLoginInfo(accessToken: String) {
        var loginJson = LoginRequest(name, accessToken)

        // 네트워크 요청 및 응답 처리
        LoginService.retrofitPostLogin(loginJson)
            .enqueue(object : Callback<LoginResponse> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        val userId = responseData?.user?.userId
                        saveUserIdToSharedPreferences(applicationContext, userId)
                        println("로그인 성공: $responseData")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("로그인 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("POST ERROR", "실패원인: $t")
                }
            })
    }


}
