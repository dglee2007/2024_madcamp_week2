package com.example.a2024_madcamp_week2.utility

import android.content.Context
import android.content.SharedPreferences

private const val SHARED_PREF_NAME = "my_shared_preferences"
private const val KEY_USER_ID = "user_id"

// userId를 SharedPreferences에 저장하는 함수
fun saveUserIdToSharedPreferences(context: Context, userId: Int?) {
    // SharedPreferences 객체 가져오기
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    // SharedPreferences를 수정하기 위한 Editor 객체 가져오기
    val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // userId를 SharedPreferences에 저장
    editor.putInt(KEY_USER_ID, userId ?: -1) // 기본값으로 -1을 설정하거나 null 처리 가능
    editor.apply()
}

// SharedPreferences에서 userId를 가져오는 함수
fun getUserIdFromSharedPreferences(context: Context): Int {
    // SharedPreferences 객체 가져오기
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    // 저장된 userId 가져오기 (기본값으로 -1을 설정)
    return sharedPreferences.getInt(KEY_USER_ID, -1)
}