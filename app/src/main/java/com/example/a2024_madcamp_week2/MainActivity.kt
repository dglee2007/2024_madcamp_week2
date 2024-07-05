package com.example.a2024_madcamp_week2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.a2024_madcamp_week2.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
                // 로그인 된 경우 MyPageActivity로 이동
                val intent = Intent(this@MainActivity, MyPageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val keyHash=Utility.getKeyHash(this)
        Log.d("Hash",keyHash)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun getHashKey() {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            } else {
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            }

            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo.apkContentsSigners
            } else {
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
}
