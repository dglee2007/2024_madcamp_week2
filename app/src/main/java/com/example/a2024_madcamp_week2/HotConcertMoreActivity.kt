package com.example.a2024_madcamp_week2

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2024_madcamp_week2.adapter.HotConcertMoreAdapter
import com.example.a2024_madcamp_week2.api.HomeService
import com.example.a2024_madcamp_week2.api.HotConcertResponse
import com.example.a2024_madcamp_week2.databinding.ActivityHotConcertMoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        val hotAdapter = HotConcertMoreAdapter(hotConcertList)
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
}