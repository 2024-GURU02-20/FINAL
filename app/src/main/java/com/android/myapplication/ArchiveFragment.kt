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

class ArchiveFragment : Fragment() {
    private lateinit var binding: FragmentArchiveBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        binding = FragmentArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    class ArchiveFragment : Fragment() {
        private lateinit var binding: FragmentArchiveBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentArchiveBinding.inflate(inflater, container, false)

            binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)

                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.dayText.text = day.date.dayOfMonth.toString()
                }
            }

            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(6) // 6개월 전부터
            val endMonth = currentMonth.plusMonths(6) // 6개월 후까지
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

            binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
            binding.calendarView.scrollToMonth(currentMonth)

            return binding.root
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            val dayText: TextView = view.findViewById(R.id.day_text)
        }
    }
}
