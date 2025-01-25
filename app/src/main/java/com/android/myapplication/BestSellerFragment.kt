package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BestSellerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_best_seller.xml 연결
        return inflater.inflate(R.layout.fragment_best_seller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // RecyclerView 설정
//        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_bestsellerList)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = BestSellerAdapter() // 어댑터는 아래 구현 필요

        // btnGobackHome 버튼 클릭 시 이전 화면으로 이동
        val btnGobackHome: ImageButton = view.findViewById(R.id.btnGobackHome)
        btnGobackHome.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}