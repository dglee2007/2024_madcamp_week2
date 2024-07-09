package com.example.a2024_madcamp_week2.ui.mypage

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.a2024_madcamp_week2.R
import com.example.a2024_madcamp_week2.api.PlanResponse
import com.example.a2024_madcamp_week2.api.PlanService
import com.example.a2024_madcamp_week2.utility.getUserIdFromSharedPreferences
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MypageFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private val events = mutableSetOf<CalendarDay>()
    private val myPlanList = ArrayList<PlanResponse>()
    private lateinit var planView: LinearLayout // planView LinearLayout 참조

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

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
}