package com.example.a2024_madcamp_week2.utility

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "b0fce2ef6cde839710cbaff0835b2c44")    }
}