package com.example.a2024_madcamp_week2

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2024_madcamp_week2.adapter.ClosingSoonConcertMoreAdapter
import com.example.a2024_madcamp_week2.api.ClosingSoonConcertResponse
import com.example.a2024_madcamp_week2.api.HomeService
import com.example.a2024_madcamp_week2.databinding.ActivityClosingSoonConcertMoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClosingSoonConcertMoreActivity: AppCompatActivity() {

    private lateinit var binding: ActivityClosingSoonConcertMoreBinding
    private lateinit var closingSoonConcertList: ArrayList<ClosingSoonConcertResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClosingSoonConcertMoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        closingSoonConcertList = ArrayList() // 초기화 추가

        lifecycleScope.launch(Dispatchers.Main) {
            getClosingSoonConcerts()
            setupClosingSoonRecyclerView() // 데이터를 가져온 후 리사이클러뷰 설정
        }
    }


    private fun setupClosingSoonRecyclerView() {
        // 요즘 HOT 리사이클러뷰 설정
        binding?.closingSoonRecyclerView?.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val closingSoonConcertAdapter = ClosingSoonConcertMoreAdapter(closingSoonConcertList)
        binding?.closingSoonRecyclerView?.adapter = closingSoonConcertAdapter
    }

    private suspend fun getClosingSoonConcerts() {
        try {
            val response = withContext(Dispatchers.IO) {
                HomeService.retrofitGetClosingSoonConcerts().execute()
            }
            if (response.isSuccessful) {
                val closingSoonConcerts = response.body()
                if (closingSoonConcerts != null) {
                    withContext(Dispatchers.Main) {
                        closingSoonConcertList.clear() // 기존 리스트 초기화
                        closingSoonConcertList.addAll(closingSoonConcerts) // 데이터 추가
                        binding?.closingSoonRecyclerView?.adapter?.notifyDataSetChanged() // Adapter에 변경 사항 알림
                        Log.d("hotconcerts", closingSoonConcertList.toString())
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