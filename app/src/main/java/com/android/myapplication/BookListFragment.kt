
package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentBookListBinding
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    // ViewBinding을 통해 XML 레이아웃 파일과 연결
    private lateinit var binding: FragmentBookListBinding

    // ViewModel 초기화
    private lateinit var viewModel: AladinViewModel

    // RecyclerView용 어댑터
    private lateinit var bestSellersAdapter: BestSellersAdapter
    private lateinit var newReleasesAdapter: NewReleasesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            // 필요하면 arguments를 처리할 로직 추가
        }


        val apiKey = BuildConfig.ALADIN_API_KEY
        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookListBinding.inflate(inflater, container, false)

        // 검색창 누르면 검색 페이지로 이동
        binding.search.setOnQueryTextFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                // SearchFragment로 전환
                val searchFragment = SearchFragment()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootlayout, searchFragment)
                    .addToBackStack(null) // 뒤로 가기 스택 추가
                    .commit()
            }
        }

        // 버튼 클릭 이벤트 설정
        setupButtonListeners()

        // RecyclerView 초기화
        initRecyclerViews()

        // 데이터 로드
        fetchBooks()

        return binding.root
    }

    // RecyclerView 초기화
    private fun initRecyclerViews() {
        // 베스트셀러 RecyclerView 설정
        binding.recyclerBestseller.apply {

            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false) // 가로 스크롤
            bestSellersAdapter = BestSellersAdapter(emptyList()) // 초기값 emptyList
            adapter = bestSellersAdapter
        }

        // 신간 리스트 RecyclerView 설정
        binding.recyclerNewbook.apply {

            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false) // 가로 스크롤
            newReleasesAdapter = NewReleasesAdapter(emptyList()) // 초기값 emptyList
            adapter = newReleasesAdapter
        }
    }

    // API 호출을 통해 데이터를 가져오는 함수
    private fun fetchBooks() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch {
            try {
                // 베스트셀러 데이터 가져오기
                val bestSellersResponse = viewModel.fetchBestSellers(apiKey)
                bestSellersAdapter = BestSellersAdapter(bestSellersResponse.item) // 어댑터에 데이터 전달
                binding.recyclerBestseller.adapter = bestSellersAdapter // 어댑터 연결

                // 신간 리스트 데이터 가져오기
                val newReleasesResponse = viewModel.fetchNewReleases(apiKey)
                newReleasesAdapter = NewReleasesAdapter(newReleasesResponse.item) // 어댑터에 데이터 전달
                binding.recyclerNewbook.adapter = newReleasesAdapter // 어댑터 연결
            } catch (e: Exception) {
                e.printStackTrace() // 에러 로그 출력
            }
        }
    }

    // 버튼 클릭 이벤트 설정
    private fun setupButtonListeners() {
        // "베스트셀러" 더보기 버튼 클릭 시 BestSellerFragment로 이동
        binding.btnMoreinfo1.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, BestSellerFragment())
                .addToBackStack(null)
                .commit()
        }

        // "신간 리스트" 더보기 버튼 클릭 시 NewReleasedFragment로 이동
        binding.btnMoreinfo2.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, NewReleasedFragment())
                .addToBackStack(null)
                .commit()
        }

        // "다독왕" 더보기 버튼 클릭 시 TopReaderPickFragment로 이동
        binding.btnMoreinfo3.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, TopReaderPickFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}

