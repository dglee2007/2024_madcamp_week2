package com.example.a2024_madcamp_week2.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ReviewService {
    @Multipart
    @POST("review/create/{user_id}") // Replace with your API endpoint
    fun postReview(
        @Part image: MultipartBody.Part,
        @Part("title") title: String,
        @Part("content") content: String,
        @Part("rating") rating: Int,
        @Path("user_id") userId: Int
    ): Call<String>


    @GET("review/all")
    fun getAllReviews(
    ): Call<List<ReviewResponse>>

    @GET("review/{review_id}")
    fun getReviewDetail(
        @Path("reviewId") rid: String
    ): Call<ReviewResponse>

    @GET("review/create/{user_id}")
    fun getMyReviews(
        @Path("userId") userId: String
    ): Call<List<ReviewResponse>>

    @GET("review/keyword/{keyword}")
    fun getReviewsByKeyword(
        @Path("keyword") keyword: String
    ): Call<List<ReviewResponse>>

    companion object {
        fun create(): ReviewService {
            return ApiClient.create(ReviewService::class.java)
        }

        fun retrofitGetAllReviews(): Call<List<ReviewResponse>> {
            return ApiClient.create(ReviewService::class.java).getAllReviews()
        }

        fun retrofitGetReviewDetail(rid: String): Call<ReviewResponse> {
            return ApiClient.create(ReviewService::class.java).getReviewDetail(rid)
        }

        fun retrofitGetMyReviews(userId: String): Call<List<ReviewResponse>> {
            return ApiClient.create(ReviewService::class.java).getMyReviews(userId)
        }

        fun retrofitGetReviewsByKeyword(keyword: String): Call<List<ReviewResponse>> {
            return ApiClient.create(ReviewService::class.java).getReviewsByKeyword(keyword)
        }
    }
}