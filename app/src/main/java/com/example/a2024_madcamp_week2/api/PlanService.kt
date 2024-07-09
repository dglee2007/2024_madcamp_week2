package com.example.a2024_madcamp_week2.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PlanService {

    @POST("/plan/create/{user_id}")
    fun postPlan(
        @Body planRequest: PlanRequest,
        @Path("user_id") userId: Int
    ): Call<String>

    @GET("plan/{user_id}")
    fun getMyPlans(
        @Path("user_id") userId: Int
    ): Call<List<PlanResponse>>

    companion object{
        fun retrofitGetMyPlans(userId: Int): Call<List<PlanResponse>> {
            return ApiClient.create(PlanService::class.java).getMyPlans(userId)
        }
    }
}