package com.example.a2024_madcamp_week2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler

import android.util.Base64
import android.util.Log
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.example.a2024_madcamp_week2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.content.pm.PackageManager
import java.security.MessageDigest

import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

import android.widget.Button



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 카카오 로그인 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 로그인 안된 경우 LoginActivity로 이동
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else if (tokenInfo != null) {
                //로딩 창 보여주기
                showLoadingScreen()

                setupNavigation()

                // 지연 후 로딩 창 감추기
                Handler().postDelayed({
                    hideLoadingScreen()
                }, 1000)
            }
        }



        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)

        // getHashKey() 호출
        getHashKey()

    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_mypage
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 모든 프래그먼트 전환 시 로딩 스크린을 표시합니다.
            showLoadingScreen()

            // 프래그먼트 로드가 완료되면 로딩 스크린을 숨깁니다.
            Handler().postDelayed({
                hideLoadingScreen()
            }, 500) // 예시로 2초 지연
        }
    }

    private fun getHashKey() {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }

            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }

            for (signature in signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("KeyHash", hashKey)
            }
        } catch (e: Exception) {
            Log.e("KeyHash", "Error while getting hash key", e)
        }
    }

    private fun showLoadingScreen() {
        binding.loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoadingScreen() {
        binding.loadingLayout.visibility = View.GONE
    }
}
