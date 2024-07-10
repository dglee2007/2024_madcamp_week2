package com.example.a2024_madcamp_week2.ui.mypage

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.LogoutActivity
import com.example.a2024_madcamp_week2.R
import com.example.a2024_madcamp_week2.api.PlanResponse
import com.example.a2024_madcamp_week2.api.PlanService
import com.example.a2024_madcamp_week2.databinding.FragmentMypageBinding
import com.example.a2024_madcamp_week2.utility.getUserIdFromSharedPreferences
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.math.pow
import kotlin.math.roundToInt


class MypageFragment : Fragment() {

    private lateinit var binding: FragmentMypageBinding
    private lateinit var calendarView: MaterialCalendarView
    private val events = mutableSetOf<CalendarDay>()
    private val myPlanList = ArrayList<PlanResponse>()
    private lateinit var planView: LinearLayout // planView LinearLayout 참조
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        KakaoMapSdk.init(requireContext(), "@string/kakao_app_key");

        calendarView = view.findViewById(R.id.calendarView)
        planView = view.findViewById(R.id.planView)

        calendarView.addDecorator(EventDecorator(Color.RED, events))

        calendarView.setOnDateChangedListener { _, date, _ ->
            val selectedPlans = getPlansForDate(date)
            displayPlans(selectedPlans)
        }

        // 데이터 가져오기
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            getMyPlans()
        }

        val logoutButton: ImageView = view.findViewById(R.id.logout_button)
        logoutButton.setOnClickListener {
            val intent = Intent(activity, LogoutActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun getPlansForDate(date: CalendarDay): List<PlanResponse> {
        return myPlanList.filter {
            CalendarDay.from(it.date.toYear(), it.date.toMonth(), it.date.toDay()) == date
        }
    }

    private fun displayPlans(plans: List<PlanResponse>) {
        planView.removeAllViews() // 기존 뷰 삭제

        plans.forEach { plan ->
            val itemView = layoutInflater.inflate(R.layout.item_my_plan, null)

            val concertImageView: ImageView = itemView.findViewById(R.id.concertImageView)
            val concertTitleTextView: TextView = itemView.findViewById(R.id.concertTitleTextView)
            val concertPlaceTextView: TextView = itemView.findViewById(R.id.concertPlaceTextView)
            val concertDateTextView: TextView = itemView.findViewById(R.id.concertDateTextView)

            // 뷰에 데이터 설정
            concertTitleTextView.text = plan.concert.title
            concertPlaceTextView.text = plan.concert.place
            concertDateTextView.text = plan.concert.date

            // Glide를 사용하여 이미지 로드
            Glide.with(itemView.context)
                .load(plan.concert.imageUrl)
                .into(concertImageView)

            // itemView를 planView LinearLayout에 추가
            planView.addView(itemView)
        }
    }

    private fun String.toYear(): Int = this.substring(0, 4).toInt()
    private fun String.toMonth(): Int = this.substring(5, 7).toInt()
    private fun String.toDay(): Int = this.substring(8, 10).toInt()

    private class EventDecorator(private val color: Int, private val dates: Set<CalendarDay>) :
        DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(5F, color))
        }
    }

    private suspend fun getMyPlans() {
        try {
            val response = withContext(Dispatchers.IO) {
                PlanService.retrofitGetMyPlans(getUserIdFromSharedPreferences(requireContext())).execute()
            }
            if (response.isSuccessful) {
                val myPlans = response.body()
                if (myPlans != null) {
                    withContext(Dispatchers.Main) {
                        myPlanList.clear() // 기존 리스트 초기화
                        myPlanList.addAll(myPlans) // 데이터 추가

                        // 달력에 이벤트 날짜 추가
                        myPlanList.forEach {
                            val eventDate = CalendarDay.from(it.date.toYear(), it.date.toMonth(), it.date.toDay())
                            events.add(eventDate)
                        }
                        calendarView.invalidateDecorators()
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

    fun search(mapSearchInput: String) = runBlocking {
        val addr: String = URLEncoder.encode(mapSearchInput, "UTF-8")

        // 카카오 REST API 키
        val apiKey = "@string/kakao_app_key"

        // 검색 API 호출
        val searchUrl = "https://dapi.kakao.com/v2/local/search/keyword.json?query=$addr"
        val response = withContext(Dispatchers.IO) { sendGetRequest(searchUrl, apiKey) }

        // 검색 결과 파싱
        val places = mutableListOf<Pair<String, String>>() // 장소명과 좌표를 저장할 리스트
        val jsonObject = JSONObject(response)
        val documents = jsonObject.getJSONArray("documents")
        for (i in 0 until documents.length()) {
            val document = documents.getJSONObject(i)
            val placeName = document.getString("place_name")
            val x = document.getString("x")
            val y = document.getString("y")
            places.add(Pair(placeName, "$x,$y"))
        }

        // 첫 번째 검색 결과로 지도 이동
        if (places.isNotEmpty()) {
            val firstPlace = places[0]
            val mapUrl = "https://map.kakao.com/link/map/${firstPlace.second}"
            println("장소명: ${firstPlace.first}")
            println("좌표: ${firstPlace.second}")
            println("지도 링크: $mapUrl")

            val coordinates = firstPlace.second.split(",") // 콤마로 분리하여 리스트로 저장
            val longitude = coordinates[0].toDouble().roundTo(6)// 경도
            val latitude = coordinates[1].toDouble().roundTo(6) // 위도
            Log.d("latitude", "latitude" + latitude.toString())
            Log.d("longitude", "longitude" + longitude.toString())

//            val latLng = LatLng(latitude, longitude) // 위도와 경도를 지정하세요
//            val cameraPosition = CameraPosition(latLng, 16.0) // 중심점과 줌 레벨을 설정합니다
//            naverMap.cameraPosition = cameraPosition
        } else {
            Toast.makeText(requireContext(), "검색결과가 없습니다. 다시 검색해 주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendGetRequest(urlString: String, apiKey: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Authorization", "KakaoAK $apiKey")

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuffer()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            return response.toString()
        } else {
            throw Exception("HTTP GET request failed with response code: $responseCode")
        }
    }

    fun Double.roundTo(decimals: Int): Double {
        val factor = 10.0.pow(decimals.toDouble())
        return (this * factor).roundToInt() / factor
    }

}