package com.android.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup //
import com.android.myapplication.databinding.FragmentArchiveBinding
import com.prolificinteractive.materialcalendarview.CalendarDay

class ArchiveFragment : Fragment(R.layout.fragment_archive) {

    private var _binding: FragmentArchiveBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment와 연결된 ViewBinding을 설정
        _binding = FragmentArchiveBinding.bind(view)

        // CalendarView 가져오기
        val calendarView = binding.calendarView

        // 특정 날짜와 이미지 URL
        val targetDate = CalendarDay.from(2025, 1, 25) // 날짜 지정
        val imageUrl = "https://example.com/sample-image.jpg" // 이미지 URL

        // Glide 데코레이터 추가
        calendarView.addDecorator(GlideImageDecorator(requireContext(), targetDate, imageUrl))
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