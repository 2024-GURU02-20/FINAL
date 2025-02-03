package com.android.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.myapplication.databinding.CalendarMonthHeaderBinding
import com.android.myapplication.databinding.FragmentArchiveBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class ArchiveFragment : Fragment() {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    // 예제 데이터: 특정 날짜에 책 표지를 표시할 데이터 (임시 HashMap)
    private val bookCovers = mapOf(
        "2025-02-03" to R.drawable.exbook, // 2월 3일
        "2025-02-07" to R.drawable.exbook  // 2월 7일
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveBinding.inflate(inflater, container, false)

        try {
            // "책 추가하기" 버튼 클릭 리스너 설정
            binding.addBook.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.archive_main_container, SearchFragment())
                    .addToBackStack(null)
                    .commit()
            }

            // 캘린더 설정
            setupCalendar()

        } catch (e: Exception) {
            Log.e("ArchiveFragment", "Error in onCreateView: ${e.message}", e)
        }

        return binding.root
    }

    private fun setupCalendar() {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // 월/년 업데이트 (외부 TextView 사용)
        binding.calendarView.monthScrollListener = { month ->
            binding.monthTitle.text = "${month.yearMonth.year}년 ${month.yearMonth.monthValue}월"
        }


        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(CalendarMonthHeaderBinding..inflate(LayoutInflater.from(view.context), view as ViewGroup, false))bind(view))
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.binding.monthText.text = "${month.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.KOREAN)} ${month.yearMonth.year}"
            }
        }


        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(6)
        val endMonth = currentMonth.plusMonths(6)
        val firstDayOfWeek = DayOfWeek.MONDAY

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)
    }

    // 날짜를 위한 뷰 컨테이너 (책 표지 포함)
    class DayViewContainer(view: View) : ViewContainer(view) {
        val dayText: TextView = view.findViewById(R.id.day_text)
    }

    // 월 헤더를 위한 뷰 컨테이너
    //class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    //    val headerText: TextView = view.findViewById(R.id.monthText)
    //}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class MonthViewContainer(val binding: CalendarMonthHeaderBinding) : ViewContainer(binding.root)
}
