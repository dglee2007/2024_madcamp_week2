package com.example.a2024_madcamp_week2.api

import retrofit2.Call
import retrofit2.http.GET

interface HomeService {

    @GET("/home/hot")
    fun getHotConcerts(
    ): Call<List<HotConcertResponse>>

    @GET("/home/closingSoon")
    fun getClosingSoonConcerts(
    ): Call<List<ClosingSoonConcertResponse>>

    companion object {

        fun retrofitGetHotConcerts(): Call<List<HotConcertResponse>> {
            return ApiClient.create(HomeService::class.java).getHotConcerts()
        }

        fun retrofitGetClosingSoonConcerts(): Call<List<ClosingSoonConcertResponse>> {
            return ApiClient.create(HomeService::class.java).getClosingSoonConcerts()
        }

    }
}