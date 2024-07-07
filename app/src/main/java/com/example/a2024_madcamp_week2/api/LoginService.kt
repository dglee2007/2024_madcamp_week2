package com.example.a2024_madcamp_week2.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("/user/login")
    fun postLogin(
        @Body jsonParams: LoginRequest
    ): Call<LoginResponse>

    companion object{

        fun retrofitPostLogin(jsonParams: LoginRequest): Call<LoginResponse> {
            return ApiClient.create(LoginService::class.java).postLogin(jsonParams)
        }

    }
}