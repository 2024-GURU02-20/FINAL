package com.android.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton


class TopReaderPickFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_released, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // RecyclerView 설정
//        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_bestsellerList)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = TopReaderPick.Adapter() // 어댑터는 아래 구현 필요

        // btnGobackHome 버튼 클릭 시 이전 화면으로 이동
        val btnGobackHome: ImageButton = view.findViewById(R.id.btnGobackHome)
        btnGobackHome.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}