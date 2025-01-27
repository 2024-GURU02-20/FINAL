package com.android.myapplication

import BestSellerAdapter
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
        return inflater.inflate(R.layout.fragment_best_seller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_bestsellerList)
        setupRecyclerView()

        // API 호출 및 데이터 로드
        fetchBestSellers()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        // BestSellerAdapter 초기화
        bestSellerAdapter = BestSellerAdapter(emptyList()) { book ->
            // 책 클릭 이벤트 처리: BookInfoFragment로 이동
            val bookInfoFragment = BookInfoFragment.newInstance(
                book.cover, book.title, book.author, book.publisher
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, bookInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = bestSellerAdapter
    }

    private fun fetchBestSellers() {
        val apiKey = BuildConfig.ALADIN_API_KEY

        lifecycleScope.launch {
            try {
                val response = viewModel.fetchBestSellers(apiKey)
                bestSellerAdapter.updateBooks(response.item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
