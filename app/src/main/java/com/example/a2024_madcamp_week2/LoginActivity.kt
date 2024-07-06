package com.example.a2024_madcamp_week2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
//import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause

class LoginActivity : AppCompatActivity() {

    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setKakaoCallback()

        // 버튼 클릭 리스너 추가
        val kakaoLoginButton: ImageButton = findViewById(R.id.btn_kakao_login)
        kakaoLoginButton.setOnClickListener {
            // 카카오톡 실행 가능 여부 확인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                // 가능하다면 카카오톡으로 로그인하기
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Toast.makeText(this, "카카오톡으로 로그인 실패!", Toast.LENGTH_SHORT).show()
                        Log.e("test", "카카오톡으로 로그인 실패! ${error.message}")

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡으로 로그인 실패 시 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
                    } else if (token != null) {
                        Toast.makeText(this, "카카오톡으로 로그인 성공!", Toast.LENGTH_SHORT).show()
                        Log.d("test", "카카오톡으로 로그인 성공! ${token.accessToken}")

                        // 로그인 성공 시 메인 화면으로 이동
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                // 카카오톡이 설치되어 있지 않으면 카카오계정으로 로그인
                UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoCallback)
            }
        }
    }

    fun btnKakaoLogin(view: View) {
        // 이 메서드는 XML에서 onClick 속성에 의해 호출될 수 있습니다.
        // 하지만 이미 setOnClickListener를 설정했으므로 이 메서드는 필요 없습니다.
        // 필요에 따라 이 메서드를 삭제할 수 있습니다.
    }

    private fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) {
                when (error.toString()) {
                    AuthErrorCause.AccessDenied.toString() -> {
                        Log.d("[카카오로그인]", "접근이 거부 됨(동의 취소)")
                    }
                    AuthErrorCause.InvalidClient.toString() -> {
                        Log.d("[카카오로그인]", "유효하지 않은 앱")
                    }
                    AuthErrorCause.InvalidGrant.toString() -> {
                        Log.d("[카카오로그인]", "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    AuthErrorCause.InvalidRequest.toString() -> {
                        Log.d("[카카오로그인]", "요청 파라미터 오류")
                    }
                    AuthErrorCause.InvalidScope.toString() -> {
                        Log.d("[카카오로그인]", "유효하지 않은 scope ID")
                    }
                    AuthErrorCause.Misconfigured.toString() -> {
                        Log.d("[카카오로그인]", "설정이 올바르지 않음(android key hash)")
                    }
                    AuthErrorCause.ServerError.toString() -> {
                        Log.d("[카카오로그인]", "서버 내부 에러")
                    }
                    AuthErrorCause.Unauthorized.toString() -> {
                        Log.d("[카카오로그인]", "앱이 요청 권한이 없음")
                    }
                    else -> {
                        Log.d("[카카오로그인]", "기타 에러")
                    }
                }
            } else if (token != null) {
                Log.d("[카카오로그인]", "로그인에 성공하였습니다.\n${token.accessToken}")
                UserApiClient.instance.accessTokenInfo { tokenInfo, tokenInfoError ->
                    if (tokenInfoError != null) {
                        Log.d("[카카오로그인]", "토큰 정보 조회 실패: ${tokenInfoError}")
                    } else if (tokenInfo != null) {
                        UserApiClient.instance.me { user, userError ->
                            if (userError != null) {
                                Log.d("[카카오로그인]", "사용자 정보 요청 실패: $userError")
                            } else if (user != null) {
                                Log.d("[카카오로그인]", "사용자 정보: ${user.kakaoAccount?.profile?.nickname}")

                                // 메인 화면으로 이동
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }
            } else {
                Log.d("카카오로그인", "토큰==null error==null")
            }
        }
    }
}
