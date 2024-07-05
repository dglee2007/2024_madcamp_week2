plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.a2024_madcamp_week2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.a2024_madcamp_week2"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.core:core:1.13.1")

    // 카카오 로그인 모듈 의존성 추가
    implementation("com.kakao.sdk:v2-all:2.20.3") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.20.3") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:2.20.3") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:2.20.3") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:2.20.3") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:2.20.3") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:2.20.3") // 카카오톡 인증 서비스 API 모듈
    implementation("com.kakao.sdk:v2-auth:2.8.6")
}