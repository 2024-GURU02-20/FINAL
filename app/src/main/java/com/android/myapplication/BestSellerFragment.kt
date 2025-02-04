package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentBestSellerBinding
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class BestSellerFragment : Fragment() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var moreInfoAdapter: MoreInfoAdapter
    private lateinit var viewModel: AladinViewModel
    private lateinit var binding: FragmentBestSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBestSellerBinding.inflate(inflater, container, false
        )
        binding.customTopBar.onBackClick = {
            parentFragmentManager.popBackStack()
        }
        binding.customTopBar.setTitle("베스트 셀러")

        return binding.root
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
        // moreinfoAdapter 초기화
        moreInfoAdapter = MoreInfoAdapter(emptyList()) { book ->
            // 책 클릭 시 BookInfoFragment로 이동 (책 정보를 전달)
            val bookInfoFragment = BookInfoFragment.newInstance(
                book.cover, book.title, book.author, book.publisher, book.pubDate, book.description, book.isbn
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, bookInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = moreInfoAdapter
    }

    // API에서 베스트셀러 데이터를 가져와 RecyclerView에 업데이트하는 함수
    private fun fetchBestSellers() {
        val apiKey = BuildConfig.ALADIN_API_KEY

        lifecycleScope.launch {
            try {
                // API 호출하여 베스트셀러 데이터 가져오기
                val response = viewModel.fetchBestSellers(apiKey)
                moreInfoAdapter.updateBooks(response.item) // 어댑터에 데이터 전달
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
