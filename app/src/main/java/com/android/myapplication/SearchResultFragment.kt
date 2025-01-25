package com.android.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding.search.setQuery(searchQuery, false)

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
        val layoutManager = GridLayoutManager(context, 2)
        binding.searchResultRecyclerView.layoutManager = layoutManager

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
                    val adapter = SearchResultBookAdapter(requireContext(), items!!)
                    binding.searchResultRecyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SearchResultFragment", "Error fetching items: ${e.message}")
            }
        }
    }
}