package com.example.a2024_madcamp_week2.ui.mypage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a2024_madcamp_week2.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class MypageFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private val events = mutableSetOf<CalendarDay>() // 일정이 있는 날짜를 저장하는 Set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        calendarView = view.findViewById(R.id.calendarView)

        // 예시로 일정 데이터를 설정합니다.
        events.add(CalendarDay.today()) // 오늘 날짜에 일정이 있는 경우

        // CalendarView에 Decorator 추가하여 일정이 있는 날짜에 점을 표시합니다.
        calendarView.addDecorator(EventDecorator(Color.RED, events))

        calendarView.setOnDateChangedListener { widget, date, selected ->
            // 날짜를 클릭했을 때 할 일을 여기에 작성합니다.
        }

        return view
    }

    // 날짜에 점을 찍어서 일정이 있는 날을 표시하는 Decorator 클래스
    private class EventDecorator(private val color: Int, private val dates: Set<CalendarDay>) :
        com.prolificinteractive.materialcalendarview.DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: com.prolificinteractive.materialcalendarview.DayViewFacade) {
            view.addSpan(DotSpan(5F, color)) // 날짜 아래에 점을 표시합니다.
        }
    }
}
