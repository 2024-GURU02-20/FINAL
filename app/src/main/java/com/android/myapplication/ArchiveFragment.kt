package com.android.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.DB.User
import com.android.myapplication.databinding.FragmentArchiveBinding
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

class ArchiveFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        val reviewDao = database.reviewDao()

        // ✅ ViewModel 없이 Room 직접 호출하여 DB 데이터 확인
        CoroutineScope(Dispatchers.IO).launch {
            // 1. 사용자 추가 (테스트용)
            reviewDao.insertUser(User(0, "test@example.com", "다독왕", "profile.png"))

            // 2. 리뷰 추가 (테스트용)
            reviewDao.insertReview(Review(0, 1, "9781234567890", 4.5f, "이 책 너무 좋아!", "기억에 남는 문장", "2025-01-31", 10))

            // 3. 다독왕의 ISBN 목록 가져오기
            val isbnList = reviewDao.getIsbnListByUserId(1)
            Log.d("ArchiveFragment", "다독왕의 책 ISBN 목록: $isbnList")

            // 4. 다독왕의 리뷰 데이터 가져오기
            val reviews = reviewDao.getReviewsByUserId(1)
            for (review in reviews) {
                Log.d("ArchiveFragment", "리뷰 정보: ${review.review}")
            }
        }
    }
}













//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [ArchiveFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ArchiveFragment : Fragment() {
//    private lateinit var binding: FragmentArchiveBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentArchiveBinding.inflate(inflater, container, false)
//
//        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//
//            override fun bind(container: DayViewContainer, day: CalendarDay) {
//                container.dayText.text = day.date.dayOfMonth.toString()
//            }
//        }
//
//        val currentMonth = YearMonth.now()
//        val startMonth = currentMonth.minusMonths(6) // 6개월 전부터
//        val endMonth = currentMonth.plusMonths(6) // 6개월 후까지
//        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
//
//        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
//        binding.calendarView.scrollToMonth(currentMonth) // 현재 월로 이동
//        return binding.root
//    }
//
//    class DayViewContainer(view: View) : ViewContainer(view) {
//        val dayText: TextView = view.findViewById(R.id.day_text)
//    }
//}