package com.android.myapplication

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup //
import android.widget.ImageButton
import com.android.myapplication.databinding.FragmentArchiveBinding
import com.bumptech.glide.Glide
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class ArchiveFragment : Fragment(R.layout.fragment_archive) {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment와 연결된 ViewBinding을 설정
        _binding = binding.calendarView

        // CalendarView 가져오기
        val calendarView = view.findViewById<MaterialCalendarView>(R.id.calendarView)

        // 1월 한 달 범위 설정
        val januaryStart = CalendarDay.from(2025, Calendar.JANUARY, 1) // 1월 1일
        val januaryEnd = CalendarDay.from(2025, Calendar.JANUARY, 31) // 1월 31일

        // 1월 한 달을 범위로 설정
        calendarView.state().edit()
            .setMinimumDate(januaryStart) // 1월 1일부터 시작
            .setMaximumDate(januaryEnd)   // 1월 31일까지
            .setCalendarDisplayMode(CalendarMode.MONTHS) // 월 단위로 표시
            .commit()

        // 현재 날짜를 1월 1일로 설정
        calendarView.setCurrentDate(januaryStart)

        // 커스텀 버튼 추가 (이전 달로 이동)
        val leftArrowButton = view.findViewById<ImageButton>(R.id.calendarView)
        leftArrowButton.setImageResource(R.drawable._2025_01_23_023248) // 커스텀 이미지 설정
        leftArrowButton.setOnClickListener {
            calendarView.goToPrevious()
        }

        // 커스텀 버튼 추가 (다음 달로 이동)
        val rightArrowButton = view.findViewById<ImageButton>(R.id.calendarView)
        rightArrowButton.setImageResource(R.drawable._2025_01_23_023232) // 커스텀 이미지 설정
        rightArrowButton.setOnClickListener {
            calendarView.goToNext()
        }

        val imageUrl = "https://example.com/sample-image.jpg" // 이미지 URL
        // Glide 데코레이터 추가
        calendarView.addDecorator(GlideImageDecorator(requireContext(), january, imageUrl))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Fragment의 뷰가 파괴될 때 Binding 객체를 해제
        _binding = null
    }
}

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArchiveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArchiveFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_archive, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArciveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArchiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}