package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class BookListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃 연결
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // btnMoreinfo1 버튼의 클릭 이벤트 설정
        val btnMoreinfo1: Button = view.findViewById(R.id.btnMoreinfo1)
        btnMoreinfo1.setOnClickListener {
            // BestSellerFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, BestSellerFragment()) // rootlayout은 activity_main.xml의 FrameLayout ID
                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기
                .commit()
        }

        // btnMoreinfo2 버튼의 클릭 이벤트 설정
        val btnMoreinfo2: Button = view.findViewById(R.id.btnMoreinfo2)
        btnMoreinfo2.setOnClickListener {
            // BestSellerFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, NewReleasedFragment()) // rootlayout은 activity_main.xml의 FrameLayout ID
                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기
                .commit()
        }
        // btnMoreinfo3 버튼의 클릭 이벤트 설정
        val btnMoreinfo3: Button = view.findViewById(R.id.btnMoreinfo3)
        btnMoreinfo3.setOnClickListener {

            // BestSellerFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, TopReaderPickFragment()) // rootlayout은 activity_main.xml의 FrameLayout ID
                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기
                .commit()
        }
    }
}
