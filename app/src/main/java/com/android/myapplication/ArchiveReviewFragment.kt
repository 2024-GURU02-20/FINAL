package com.android.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.myapplication.ArchiveReviewFragment.DayViewContainer
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.DB.User
import com.android.myapplication.databinding.FragmentArchiveBinding
import com.android.myapplication.databinding.FragmentArchiveReviewBinding
import com.android.myapplication.databinding.FragmentSearchBinding
import com.android.myapplication.databinding.FragmentSearchResultBinding
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

class ArchiveReviewFragment : Fragment() {
    private lateinit var binding: FragmentArchiveReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentArchiveReviewBinding.inflate(inflater, container, false)

        binding.ReviewImageView.setOnClickListener {
            requireActivity().runOnUiThread {
                val currentFragment = parentFragmentManager.findFragmentById(R.id.archive_review_container)
                if (currentFragment !is BookInfoFragment) { // 중복 방지
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.rootlayout, BookInfoFragment()) // book_info 화면으로
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        // 저장하기 버튼 클릭 -> 저장 완료! Toast 메시지 적용, short = 2초
        binding.storeButton.setOnClickListener {
            Toast.makeText(requireContext(), "저장 완료!", Toast.LENGTH_SHORT).show()
        }

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                val currentMonth = YearMonth.now()
                container.dayText.text = day.date.dayOfMonth.toString()

                if (day.date.year == currentMonth.year && day.date.month == currentMonth.month) {
                    container.dayText.setTextColor(Color.BLACK) // 현재 월의 날짜
                } else {
                    container.dayText.setTextColor(Color.GRAY) // 이전/다음 달의 날짜
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(6) // 6개월 전부터
        val endMonth = currentMonth.plusMonths(6) // 6개월 후까지
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth) // 현재 월로 이동

        binding.calendarView.monthScrollListener = { month ->
            val yearMonth = month.yearMonth
            binding.monthTitle.text = "${yearMonth.year}년 ${yearMonth.monthValue}월"
        }

        return binding.root
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        val dayText: TextView = view.findViewById(R.id.day_text)
    }
}
