package com.example.a2024_madcamp_week2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2024_madcamp_week2.databinding.FragmentLoginBinding
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.AuthApiClient.instance
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.UserApiClient.instance
import com.kakao.sdk.user.model.User

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setKakaoCallback()

        binding.loginButton.setOnClickListener {
            btnKakaoLogin(it)
        }
    }

    private fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.d("[카카오로그인]","접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.d("[카카오로그인]","유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.d("[카카오로그인]","인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.d("[카카오로그인]","요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.d("[카카오로그인]","유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.d("[카카오로그인]","설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.d("[카카오로그인]","서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.d("[카카오로그인]","앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        Log.d("[카카오로그인]","기타 에러")
                    }
                }
            }
            else if (token != null) {
                Log.d("[카카오로그인]","로그인에 성공하였습니다.\n${token.accessToken}")
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (tokenInfo != null) {
                        UserApiClient.instance.me { user, error ->
                            if (user != null) {
                                binding.nickname.text = "닉네임: ${user.kakaoAccount?.profile?.nickname}"
                            }
                        }
                    }
                }
            }
            else {
                Log.d("카카오로그인", "토큰==null error==null")
            }
        }
    }

    private fun btnKakaoLogin(view: View) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireActivity(), callback = kakaoCallback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireActivity(), callback = kakaoCallback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
