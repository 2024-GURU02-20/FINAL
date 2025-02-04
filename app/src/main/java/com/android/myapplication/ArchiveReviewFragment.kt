package com.android.myapplication

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

class ArchiveReviewFragment : Fragment() {
    private lateinit var binding: FragmentArchiveReviewBinding
    private var selectedDate: LocalDate? = null // 선택된 날짜 저장 변수

    private lateinit var isbn: String
    private lateinit var coverUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            isbn = it.getString(ArchiveReviewFragment.ARG_ISBN) ?: ""
            coverUrl = it.getString(ArchiveReviewFragment.ARG_COVER_URL) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArchiveReviewBinding.inflate(inflater, container, false)

        binding.ReviewImageView.setOnClickListener {
            requireActivity().runOnUiThread {
                val currentFragment = parentFragmentManager.findFragmentById(R.id.archive_review_container)
                if (currentFragment !is BookInfoFragment) {
                    // 선택된 날짜 초기화
                    selectedDate = null
                    binding.calendarView.notifyCalendarChanged() // 캘린더 UI 업데이트

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.rootlayout, BookInfoFragment()) // book_info 화면으로 이동
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        // 저장하기 버튼 클릭 -> 저장 완료! Toast 메시지
        binding.storeButton.setOnClickListener {
            Toast.makeText(requireContext(), "저장 완료!", Toast.LENGTH_SHORT).show()
        }

        // ✅ 캘린더 날짜 설정
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                val currentMonth = YearMonth.now()
                container.dayText.text = day.date.dayOfMonth.toString()

                if (day.date.year == currentMonth.year && day.date.month == currentMonth.month) {
                    container.dayText.setTextColor(Color.BLACK) // 현재 월 날짜
                } else {
                    container.dayText.setTextColor(Color.GRAY) // 이전/다음 달 날짜
                }

                // ✅ 선택한 날짜 스타일 (배경 #FFF9ED, 테두리 #EEB64E)
                if (day.date == selectedDate) {
                    val backgroundDrawable = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor(Color.parseColor("#FFF9ED")) // 배경색
                        setStroke(5, Color.parseColor("#EEB64E")) // 테두리
                        cornerRadius = 20f
                    }
                    container.dayText.background = backgroundDrawable
                    container.dayText.setTextColor(Color.BLACK)
                } else {
                    container.dayText.setBackgroundColor(Color.TRANSPARENT)
                }

                // ✅ 날짜 아래 이미지 초기화
                container.dayImage.visibility = View.GONE

                // ✅ 특정 날짜(예: 2025년 2월 5일)에 이미지 표시
                if (day.date == LocalDate.of(2025, 2, 5)) {
                    container.dayImage.visibility = View.VISIBLE
                }

                // ✅ 날짜 클릭 시 DatePicker 실행
                container.dayText.setOnClickListener {
                    showDatePicker(day.date.year, day.date.monthValue, day.date.dayOfMonth)
                }
            }
        }

        // ✅ 캘린더 세팅
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

    // ✅ DatePickerDialog 실행 함수
    private fun showDatePicker(year: Int, month: Int, day: Int) {
        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDateValue = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)

            selectedDate = selectedDateValue // 선택된 날짜 업데이트
            binding.calendarView.notifyCalendarChanged() // 캘린더 UI 업데이트

            Toast.makeText(requireContext(), "선택한 날짜: $selectedDateValue", Toast.LENGTH_SHORT).show()
        }, year, month - 1, day)

        datePicker.show()
    }

    // ✅ DayViewContainer 추가
    class DayViewContainer(view: View) : ViewContainer(view) {
        val dayText: TextView = view.findViewById(R.id.day_text)
        val dayImage: ImageView = view.findViewById(R.id.day_image)
    }

    companion object {
        // 데이터 키 값
        private const val ARG_ISBN = "isbn"
        private const val ARG_COVER_URL = "coverUrl"

        // 인스턴스 생성 메서드
        fun newInstance(
            isbn: String, coverUrl: String
        ) = BookInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ISBN, isbn)
            }
        }
    }
}
