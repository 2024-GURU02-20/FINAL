package com.android.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Size
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.myapplication.databinding.FragmentArchiveBinding
import com.google.firebase.auth.FirebaseAuth
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.view.DaySize
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.MonthScrollListener
import java.time.LocalDate

class ArchiveFragment : Fragment() {
    private lateinit var binding: FragmentArchiveBinding
    private var selectedDate: LocalDate? = null // 선택된 날짜 저장 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentArchiveBinding.inflate(inflater, container, false)

        binding.addBook.setOnClickListener {
            requireActivity().runOnUiThread {
                val currentFragment =
                    parentFragmentManager.findFragmentById(R.id.archive_main_container)
                if (currentFragment !is SearchFragment) { // 중복 방지
                    // 선택된 날짜 초기화
                    selectedDate = null
                    binding.calendarView.notifyCalendarChanged() // 캘린더 UI 업데이트

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.rootlayout, SearchFragment()) // search 화면으로
                        .addToBackStack(null)
                        .commit()
                }
            }
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

                // 선택한 날짜에 노란 배경 적용
                if (day.date == selectedDate) {
                    val backgroundDrawable = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        setColor(Color.parseColor("#FFF9ED")) // 배경색 #FFF9ED
                        setStroke(5, Color.parseColor("#EEB64E")) // 테두리색 #EEB64E (두께 5px)
                        cornerRadius = 20f // 모서리 둥글게
                    }
                    container.dayText.background = backgroundDrawable
                    container.dayText.setTextColor(Color.BLACK)
                } else {
                    container.dayText.setBackgroundColor(Color.TRANSPARENT) // 기본 배경
                   // container.dayText.setTextColor(Color.BLACK) // 기본 글씨
                }
                // 날짜 이미지 기본값: 이미지 없음 (초기화)
                container.dayImage.visibility = View.GONE

                // 특정 날짜에만 이미지 표시 (예: 2025년 2월 5일)
                if (day.date == LocalDate.of(2025, 2, 5)) {
                    container.dayImage.visibility = View.VISIBLE
                }


                // 날짜 클릭 이벤트 -> DatePickerDialog 실행
                container.dayText.setOnClickListener {
                    showDatePicker(day.date.year, day.date.monthValue, day.date.dayOfMonth)
                }
            }
        }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(6) // 6개월 전부터
        val endMonth = currentMonth.plusMonths(6) // 6개월 후까지
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek


        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

        binding.calendarView.monthScrollListener = { month ->
            val yearMonth = month.yearMonth
            binding.monthTitle.text = "${yearMonth.year}년 ${yearMonth.monthValue}월"
        }


        binding.customProfileView.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }

    // ✅  DatePickerDialog 실행 함수 추가
    private fun showDatePicker(year: Int, month: Int, day: Int) {
        val datePicker =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateValue = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)

                selectedDate = selectedDateValue // 선택된 날짜 업데이트
                binding.calendarView.notifyCalendarChanged() // 캘린더 UI 업데이트

                Toast.makeText(requireContext(), "선택한 날짜: $selectedDateValue", Toast.LENGTH_SHORT)
                    .show()
            }, year, month - 1, day)

        datePicker.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.customProfileView.setData(user.displayName + "님의", "독서 기록입니다", user.photoUrl)
        }
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        val dayText: TextView = view.findViewById(R.id.day_text)
        val dayImage: ImageView = view.findViewById(R.id.day_image) // 날짜칸에 이미지 추가

    }
//        init {
//            dayText.layoutParams.width = 47 // 가로 47px
//            dayText.layoutParams.height = 68 // 세로 68px
//        }
}


