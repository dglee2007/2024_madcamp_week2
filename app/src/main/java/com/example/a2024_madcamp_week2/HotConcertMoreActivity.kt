package com.example.a2024_madcamp_week2

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2024_madcamp_week2.adapter.ClosingSoonConcertMoreAdapter
import com.example.a2024_madcamp_week2.adapter.HotConcertMoreAdapter
import com.example.a2024_madcamp_week2.api.ApiClient
import com.example.a2024_madcamp_week2.api.HomeService
import com.example.a2024_madcamp_week2.api.HotConcertResponse
import com.example.a2024_madcamp_week2.api.PlanRequest
import com.example.a2024_madcamp_week2.api.PlanService
import com.example.a2024_madcamp_week2.databinding.ActivityHotConcertMoreBinding
import com.example.a2024_madcamp_week2.utility.getUserIdFromSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HotConcertMoreActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHotConcertMoreBinding
    private lateinit var hotConcertList: ArrayList<HotConcertResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHotConcertMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hotConcertList = ArrayList() // 초기화 추가

        lifecycleScope.launch(Dispatchers.Main) {
            getHotConcerts()
            setupHotRecyclerView() // 데이터를 가져온 후 리사이클러뷰 설정
        }
    }


    private fun setupHotRecyclerView() {
        // 요즘 HOT 리사이클러뷰 설정
        binding?.hotRecyclerView?.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val hotAdapter = HotConcertMoreAdapter(hotConcertList, object : HotConcertMoreAdapter.OnAddEventClickListener {
            override fun onAddEventClicked(concertId: Int, dateString: String) {
                // 이벤트 클릭 시 동작할 내용 정의
                postPlanFun(concertId, dateString)
            }
        })
        binding?.hotRecyclerView?.adapter = hotAdapter
    }

    private suspend fun getHotConcerts() {
        try {
            val response = withContext(Dispatchers.IO) {
                HomeService.retrofitGetHotConcerts().execute()
            }
            if (response.isSuccessful) {
                val hotConcerts = response.body()
                if (hotConcerts != null) {
                    withContext(Dispatchers.Main) {
                        hotConcertList.clear() // 기존 리스트 초기화
                        hotConcertList.addAll(hotConcerts) // 데이터 추가
                        binding?.hotRecyclerView?.adapter?.notifyDataSetChanged() // Adapter에 변경 사항 알림
                        Log.d("hotconcerts", hotConcertList.toString())
                    }
                } else {
                    val body = response.errorBody()?.string()
                    Log.e(ContentValues.TAG, "body : $body")
                }
            } else {
                Log.e(ContentValues.TAG, "Response not successful: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Exception occurred: ${e.message}", e)
        }
    }

    private fun postPlanFun(concertId: Int, dateString: String) {
        val apiService = ApiClient.create(PlanService::class.java)

        val planRequest = PlanRequest(
            concertId = concertId,
            date = dateString
        )
        // 네트워크 요청 및 응답 처리
        val call = apiService.postPlan(
            planRequest = planRequest,
            userId = getUserIdFromSharedPreferences(applicationContext)
        )
        Log.d("보내기", planRequest.toString())
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Log.d("일정 등록 성공: ", responseData.toString())
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("일정 등록 실패: ", errorBody.toString())
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("TAG", "실패원인: $t")
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
    }
}