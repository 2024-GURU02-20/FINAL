package com.android.myapplication

import BestSellerAdapter
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentBookListBinding
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding
    private lateinit var viewModel: AladinViewModel

    // RecyclerView에서 사용할 Adapter 선언
    private lateinit var bestSeller: BookListAdapter
    private lateinit var newReleased: BookListAdapter

    /////
    private lateinit var topReader: BookListAdapter // 다독왕 책 리스트용 어댑터
    /////


    ////////
    private lateinit var bestReviewAdapter: BestReviewAdapter
    ////////

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

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            binding.customProfileView.setData( user.displayName + "님!", "안녕하세요,", user.photoUrl)
        }


        // 책 아카이빙 버튼 클릭 시 네비게이션 바에서 아카이빙 선택하도록
        binding.homebtnGotoArchive.setOnClickListener {
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNavigationView.selectedItemId = R.id.archive
        }

        // 책 추천 버튼 클릭 시  네비게이션 바에서 추천으로 이동
        binding.homebtnGotoRecommend.setOnClickListener {
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNavigationView.selectedItemId = R.id.recommend
        }

        // RecyclerView 초기화
        initRecyclerViews()

        setupButtonListeners()

        // API 데이터 로드
        fetchBooks()

        /////
        fetchTopReaderBooks() // 다독왕 책 가져오기
        /////

        /////////
        fetchTopReviews() // 📌 베스트 리뷰 가져오기 추가
        /////////

        return binding.root
    }

    // RecyclerView를 초기화하는 함수
    private fun initRecyclerViews() {
        binding.recyclerBestseller.apply {
            // 베스트셀러 리스트 RecyclerView 설정
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            bestSeller = BookListAdapter(emptyList()) { book ->
                // 책 클릭 시 BookInfoFragment로 이동 (책 정보를 전달)
                val bookInfoFragment = BookInfoFragment.newInstance(
                    book.cover, book.title, book.author, book.publisher, book.pubDate, book.description
                )
                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootlayout, bookInfoFragment)
                    .addToBackStack(null)
                    .commit()
            }
            adapter = bestSeller
        }

        // 신간 리스트 RecyclerView 설정
        binding.recyclerNewbook.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            newReleased = BookListAdapter(emptyList()) { book ->
                val bookInfoFragment = BookInfoFragment.newInstance(
                    book.cover, book.title, book.author, book.publisher, book.pubDate, book.description
                )
                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootlayout, bookInfoFragment)
                    .addToBackStack(null)
                    .commit()
            }
            adapter = newReleased
        }


        /////
        // 다독왕 RecyclerView 설정
        binding.recyclerMostread.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
            topReader= BookListAdapter(emptyList()) { book ->
                val bookInfoFragment = BookInfoFragment.newInstance(
                    book.cover, book.title, book.author, book.publisher, book.pubDate, book.description
                )
                parentFragmentManager.beginTransaction()
                    .replace(R.id.rootlayout, bookInfoFragment)
                    .addToBackStack(null)
                    .commit()
            }
            adapter = topReader
        }


        //////
        binding.recyclerBestReview.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            bestReviewAdapter = BestReviewAdapter(emptyList())
            adapter = bestReviewAdapter
        }
        ///////////

    }

    // API에서 데이터를 가져와 RecyclerView에 업데이트하는 함수
    private fun fetchBooks() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch {
            try {
                // 베스트셀러 데이터 가져오기
                val bestSellersResponse = viewModel.fetchBestSellers(apiKey)
                bestSeller.updateBooks(bestSellersResponse.item)

                // 신간 리스트 데이터 가져오기
                val newReleasesResponse = viewModel.fetchNewReleases(apiKey)
                newReleased.updateBooks(newReleasesResponse.item)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    //////
    // 다독왕의 책 목록 가져오기
    //private fun fetchTopReaderBooks() {
    fun fetchTopReaderBooks() {
        val database = AppDatabase.getDatabase(requireContext())
        val reviewDao = database.reviewDao()

        lifecycleScope.launch {
            try {
                val topReaderIsbnList = reviewDao.getTopReaderBooks()
                val bookList = mutableListOf<BookItem>()

                for (isbn in topReaderIsbnList) {
                    val response = viewModel.searchBooks(BuildConfig.ALADIN_API_KEY, isbn)
                    if (response.item.isNotEmpty()) {
                        bookList.add(response.item[0]) // 첫 번째 검색 결과 추가
                    }
                }

                topReader.updateBooks(bookList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    /////

    //////////
    private fun fetchTopReviews() {
        val database = AppDatabase.getDatabase(requireContext())
        val reviewDao = database.reviewDao()

        lifecycleScope.launch {
            try {
                val topReviews = reviewDao.getTopLikedReviews() // 추천 많은 순으로 3개 가져오기
                val bookList = mutableListOf<BookItem>()

                for (review in topReviews) {
                    val response = viewModel.searchBooks(BuildConfig.ALADIN_API_KEY, review.isbn)
                    if (response.item.isNotEmpty()) {
                        bookList.add(response.item[0]) // 첫 번째 검색 결과 추가
                    }
                }

                bestReviewAdapter.updateReviews(bookList) // 어댑터에 데이터 전달
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    ////////////


    // 버튼 클릭 이벤트 설정
    private fun setupButtonListeners() {
        // "베스트셀러 더보기" 버튼 클릭 시 BestSellerFragment로 이동
        binding.btnMoreinfo1.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, BestSellerFragment())
                .addToBackStack(null)
                .commit()
        }

        // "신간 리스트 더보기" 버튼 클릭 시 NewReleasedFragment로 이동
        binding.btnMoreinfo2.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, NewReleasedFragment())
                .addToBackStack(null)
                .commit()
        }

        // "다독왕 선정 더보기" 버튼 클릭 시 TopReaderPickFragment로 이동
        binding.btnMoreinfo3.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, TopReaderFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.customProfileView.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }

        }
    }
}

