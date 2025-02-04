package com.android.myapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentSearchResultBinding
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Query
import kotlin.math.log
class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var viewModel: AladinViewModel
    private var items: AladinResponse? = null // 초기값을 null로 설정
    private lateinit var searchQuery: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        searchQuery = arguments?.getString("search_query") ?: ""

        val apiKey = BuildConfig.ALADIN_API_KEY
        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        // SearchView 초기화
        binding.search.post {
            binding.search.setQuery(searchQuery, false) // 검색어 설정 (두 번째 인자는 submit 여부)
            binding.search.clearFocus() // 포커스를 제거해서 UI를 기본 상태로 유지
        }

        binding.customTopBar.onBackClick = {
            parentFragmentManager.popBackStack()
        }

        binding.customTopBar.setTitle("검색")

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) { // 값이 비어있지 않은 경우만 검색어 변경
                        searchQuery = it

                        fetchItems()

                    } else {
                        binding.search.setQuery("", false)
                        binding.search.clearFocus()
                        binding.search.isIconified = false
                        binding.search.queryHint = "검색어를 입력해주세요."
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 변경 이벤트 처리 (필요한 경우)
                return false
            }
        })

        // RecyclerView 초기화
        val layoutManager = GridLayoutManager(context, 2) // 2열 Grid 설정
        val recyclerView = binding.searchResultRecyclerView
        recyclerView.layoutManager = layoutManager

        // 16dp를 px로 변환 (dp → px 변환)
        val spacingInDp = 16
        val spacingInPx = (spacingInDp * resources.displayMetrics.density).toInt()

        // ItemDecoration 추가 (아이템 사이 간격 16dp 적용, 가장자리 제외)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacingInPx))

        // 비동기로 데이터를 로드하고 RecyclerView 업데이트
        fetchItems()

        return binding.root
    }

    private fun fetchItems() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch {
            try {
                val response = viewModel.searchBooks(apiKey, searchQuery)
                items = response // 데이터를 로드한 후 items 초기화

                // UI 업데이트 (main thread에서 RecyclerView 설정)
                if (isAdded && items != null) {
                    val adapter = SearchResultBookAdapter(requireContext(), items!! ) { book ->
                        // 책 클릭 시 BookInfoFragment로 이동 (책 정보를 전달)
                        val bookInfoFragment = BookInfoFragment.newInstance(
                            book.cover, book.title, book.author, book.publisher, book.pubDate, book.description, book.isbn
                        )
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.rootlayout, bookInfoFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    binding.searchResultRecyclerView.adapter = adapter

                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SearchResultFragment", "Error fetching items: ${e.message}")
            }
        }
    }
}