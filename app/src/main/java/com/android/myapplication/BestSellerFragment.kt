package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class BestSellerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bestSellerAdapter: BestSellerAdapter
    private lateinit var viewModel: AladinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewModel 초기화
        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)
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

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_bestsellerList)
        setupRecyclerView()

        // "뒤로가기" 버튼 클릭 이벤트
        val btnGobackHome: ImageButton = view.findViewById(R.id.btnGobackHome)
        btnGobackHome.setOnClickListener {
            // 이전 화면으로 돌아가기
            parentFragmentManager.popBackStack()
        }

        // API 호출 및 데이터 로드
        fetchBestSellers()
    }

    private fun setupRecyclerView() {
        // 3열의 GridLayoutManager 설정
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        bestSellerAdapter = BestSellerAdapter(emptyList())
        recyclerView.adapter = bestSellerAdapter
    }

    private fun fetchBestSellers() {
        val apiKey = BuildConfig.ALADIN_API_KEY

        lifecycleScope.launch {
            try {
                // 베스트셀러 API 호출
                val response: AladinResponse = viewModel.fetchBestSellers(apiKey)

                // 데이터 업데이트
                bestSellerAdapter.updateBooks(response.item)
            } catch (e: Exception) {
                e.printStackTrace() // 에러 처리
            }
        }
    }
}
