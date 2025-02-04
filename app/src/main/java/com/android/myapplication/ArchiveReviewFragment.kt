package com.android.myapplication

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.databinding.FragmentArchiveReviewBinding
import com.android.myapplication.repository.ReviewRepository
import com.android.myapplication.viewmodel.ReviewViewModel
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale
import java.util.Date

class ArchiveReviewFragment : Fragment() {
    private var _binding: FragmentArchiveReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewViewModel by viewModels() // ✅ ViewModel 연결
    private lateinit var repository: ReviewRepository

    private var selectedDate: LocalDate? = null // 선택된 날짜 저장 변수
    private val selectedIsbn = "9781234567890"  // 사용자가 선택한 책의 ISBN (테스트용)
    private val selectedBookTitle = "테스트용 책 제목"  // 실제 데이터에서는 API 또는 DB에서 가져와야 함

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArchiveReviewBinding.inflate(inflater, container, false)

        val database = AppDatabase.getDatabase(requireContext())
        repository = ReviewRepository(database.reviewDao())

        // 사용자가 선택한 책의 리뷰 가져오기
        viewModel.fetchReviewsByIsbn(selectedIsbn)

        // UI 업데이트 (선택한 책의 리뷰 반영)
        lifecycleScope.launch {
            viewModel.selectedBookReview.collect { review ->
                if (review != null) {
                    binding.bookDetail.text = "책 제목: $selectedBookTitle"  // ✅ 책 제목 표시
                    binding.reviewInput.setText(review.review)
                    binding.favoriteLineInput.setText("마음에 드는 구절: ${review.favoriteLine}")
                    binding.ratingBar.rating = review.starRate
                } else {
                    binding.bookDetail.text = "등록된 리뷰 없음"
                    binding.reviewInput.setText("")
                    binding.favoriteLineInput.setText("")
                    binding.ratingBar.rating = 0f
                }
            }
        }

        // ✅ 책 리뷰 화면 이동
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

        // ✅ 리뷰 저장 버튼 클릭 시
        binding.storeButton.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val currentDate = sdf.format(Date())

            val newReview = Review(
                reviewId = 0,  // Room에서 자동 생성
                userId = 1, // 테스트용 사용자 ID
                isbn = selectedIsbn, // 선택한 책의 ISBN
                starRate = binding.ratingBar.rating,
                review = binding.reviewInput.text.toString(),
                favoriteLine = binding.favoriteLineInput.text.toString(),
                createdAt = currentDate,
                likeCount = 0
            )
            viewModel.addReview(newReview)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
