package com.android.myapplication

import BestSellerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentBookListBinding
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding
    private lateinit var viewModel: AladinViewModel
    private lateinit var bestSellerAdapter: BestSellerAdapter
    private lateinit var newReleasedAdapter: NewReleasedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookListBinding.inflate(inflater, container, false)

        // 검색창 클릭 시 SearchFragment로 이동
        binding.search.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val searchFragment = SearchFragment()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootlayout, searchFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // RecyclerView 초기화
        initRecyclerViews()

        // 버튼 클릭 이벤트 설정
        setupButtonListeners()

        // API 데이터 로드
        fetchBooks()

        return binding.root
    }

    private fun initRecyclerViews() {
        // 베스트셀러 RecyclerView 설정
        binding.recyclerBestseller.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
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
            adapter = bestSellerAdapter
        }

        // 신간 리스트 RecyclerView 설정
        binding.recyclerNewbook.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            newReleasedAdapter = NewReleasedAdapter(emptyList())
            adapter = newReleasedAdapter
        }
    }

    private fun fetchBooks() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch{
            try {
                // 베스트셀러 데이터 로드
                val bestSellersResponse = viewModel.fetchBestSellers(apiKey)
                bestSellerAdapter.updateBooks(bestSellersResponse.item)

                // 신간 데이터 로드
                val newReleasesResponse = viewModel.fetchNewReleases(apiKey)
                newReleasedAdapter.updateBooks(newReleasesResponse.item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupButtonListeners() {
        binding.btnMoreinfo1.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, BestSellerFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnMoreinfo2.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, NewReleasedFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnMoreinfo3.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, TopReaderPickFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}
