package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class BestSellerFragment : Fragment() {



    lateinit var binding: BestSellerFragment


    //알라딘 연결 및 객체 참조
    private lateinit var viewModel: AladinViewModel
    lateinit var items: AladinResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val apiKey = BuildConfig.ALADIN_API_KEY
        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)

//        // 아래의 코드를 통해 api 호출 가능
//        lifecycleScope.launch {
//            try {
//                // fetchBestSellers 호출
//                val response = viewModel.fetchBestSellers(apiKey)
//                response.item.forEach { book ->
//                    println("Title: ${book.title}")
//                    println("Author: ${book.author}")
//                    println("Publisher: ${book.publisher}")
//                    println("ISBN: ${book.isbn}")
//                    println("----------")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace() // 에러 처리
//            }
//        }
    }


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