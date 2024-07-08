package com.example.a2024_madcamp_week2.ui.home

import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2024_madcamp_week2.R
import com.example.a2024_madcamp_week2.adapter.BannerAdapter
import com.example.a2024_madcamp_week2.adapter.ClosingSoonConcertAdapter
import com.example.a2024_madcamp_week2.adapter.HotConcertAdapter
import com.example.a2024_madcamp_week2.api.ClosingSoonConcertResponse
import com.example.a2024_madcamp_week2.api.HomeService
import com.example.a2024_madcamp_week2.api.HotConcertResponse
import com.example.a2024_madcamp_week2.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import java.util.TimerTask


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val handler = Handler()
    private var hotCurrentPosition = 0
    private var closingSoonCurrentPosition = 0
    private var hotTimer: Timer? = null
    private var closingSoonTimer: Timer? = null

    private lateinit var hotConcertList: ArrayList<HotConcertResponse>
    private lateinit var closingSoonConcertList: ArrayList<ClosingSoonConcertResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        hotConcertList = ArrayList() // 초기화 추가
        closingSoonConcertList = ArrayList() // 초기화 추가

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            getHotConcerts()
            setupHotRecyclerView() // getAllReviews가 완료된 후에 RecyclerView 설정
            getClosingSoonConcerts()
            setupClosingSoonRecyclerView()
        }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        startAutoScroll()
    }

    private fun setupViewPager() {
        // ViewPager 설정
        val bannerImages = listOf(
            R.drawable.banner_item_1,
            R.drawable.banner_item_2
        )
        val bannerAdapter = BannerAdapter(requireContext(), bannerImages)
        binding?.viewPager?.adapter = bannerAdapter
    }

    private fun setupHotRecyclerView() {
        // 요즘 HOT 리사이클러뷰 설정
        binding?.hotRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val hotAdapter = HotConcertAdapter(hotConcertList)
        binding?.hotRecyclerView?.adapter = hotAdapter
    }

    private fun setupClosingSoonRecyclerView() {
        // 임박 마감 리사이클러뷰 설정
        binding?.closingSoonRecyclerView?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val closingSoonAdapter = ClosingSoonConcertAdapter(closingSoonConcertList)
        binding?.closingSoonRecyclerView?.adapter = closingSoonAdapter
    }

    private fun startAutoScroll() {
        // ViewPager 자동 스크롤
        handler.postDelayed(object : Runnable {
            override fun run() {
                binding?.viewPager?.let {
                    val itemCount = it.adapter?.count ?: 0
                    val currentItem = it.currentItem
                    val nextItem = (currentItem + 1) % itemCount
                    it.setCurrentItem(nextItem, true)
                }
                handler.postDelayed(this, 3000) // 3초마다 변경
            }
        }, 3000)

        // 요즘 HOT 리사이클러뷰 자동 스크롤
        hotTimer = Timer()
        hotTimer?.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    binding?.hotRecyclerView?.let {
                        val itemCount = it.adapter?.itemCount ?: 0
                        if(itemCount > 0) {
                            hotCurrentPosition = (hotCurrentPosition + 1) % itemCount
                            it.smoothScrollToPosition(hotCurrentPosition)
                        }
                    }
                }
            }
        }, 3000, 3000) // 3초마다 변경

        // 임박 마감 리사이클러뷰 자동 스크롤
        closingSoonTimer = Timer()
        closingSoonTimer?.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    binding?.closingSoonRecyclerView?.let {
                        val itemCount = it.adapter?.itemCount ?: 0
                        if (itemCount > 0) {
                            closingSoonCurrentPosition = (closingSoonCurrentPosition + 1) % itemCount
                            it.smoothScrollToPosition(closingSoonCurrentPosition)
                        }
                    }
                }
            }
        }, 3000, 3000) // 3초마다 변경
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        hotTimer?.cancel()
        hotTimer?.purge()
        closingSoonTimer?.cancel()
        closingSoonTimer?.purge()
        binding = null
    }

//    private suspend fun getHotConcerts() {
//        try {
//            val response = withContext(Dispatchers.IO) {
//                HomeService.retrofitGetHotConcerts().execute()
//            }
//            if (response.isSuccessful) {
//                val hotConcerts = response.body()
//                if (hotConcerts != null) {
//                    withContext(Dispatchers.Main) {
//                        hotConcertList.clear() // 기존 리스트 초기화
//                        for (hotConcert in hotConcerts) {
//                            val concertId = hotConcert.concertId
//                            val title = hotConcert.title
//                            val place = hotConcert.place
//                            val date = hotConcert.date
//                            val imageUrl = hotConcert.imageUrl
//                            hotConcertList.add(HotConcertResponse(concertId = concertId, title = title, place = place, date = date, imageUrl = imageUrl))
//                        }
//                        // RecyclerView 업데이트
//                        binding?.hotRecyclerView?.adapter?.notifyDataSetChanged()
//                        Log.d("hotconcerts", hotConcertList.toString())
//                    }
//                } else {
//                    val body = response.errorBody()?.string()
//                    Log.e(ContentValues.TAG, "body : $body")
//                }
//            } else {
//                Log.e(ContentValues.TAG, "Response not successful: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Log.e(ContentValues.TAG, "Exception occurred: ${e.message}", e)
//        }
//    }
//
//    private suspend fun getClosingSoonConcerts() {
//        try {
//            val response = withContext(Dispatchers.IO) {
//                HomeService.retrofitGetClosingSoonConcerts().execute()
//            }
//            if (response.isSuccessful) {
//                val closingSoonConcerts = response.body()
//                if (closingSoonConcerts != null) {
//                    withContext(Dispatchers.Main) {
//                        closingSoonConcertList.clear() // 기존 리스트 초기화
//                        for (closingSoonConcert in closingSoonConcerts) {
//                            val concertId = closingSoonConcert.concertId
//                            val title = closingSoonConcert.title
//                            val place = closingSoonConcert.place
//                            val date = closingSoonConcert.date
//                            val imageUrl = closingSoonConcert.imageUrl
//                            closingSoonConcertList.add(ClosingSoonConcertResponse(concertId = concertId, title = title, place = place, date = date, imageUrl = imageUrl))
//                        }
//                        // RecyclerView 업데이트
//                        binding?.closingSoonRecyclerView?.adapter?.notifyDataSetChanged()
//                        Log.d("hotconcerts", closingSoonConcertList.toString())
//                    }
//                } else {
//                    val body = response.errorBody()?.string()
//                    Log.e(ContentValues.TAG, "body : $body")
//                }
//            } else {
//                Log.e(ContentValues.TAG, "Response not successful: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Log.e(ContentValues.TAG, "Exception occurred: ${e.message}", e)
//        }
//    }

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
                        Log.d("closingssoonconcerts", closingSoonConcertList.toString())
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