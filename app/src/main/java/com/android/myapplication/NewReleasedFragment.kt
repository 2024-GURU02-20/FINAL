package com.android.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch


class NewReleasedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newReleasedAdapter: NewReleasedAdapter
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
        return inflater.inflate(R.layout.fragment_new_released, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_newreleasedList)
        setupRecyclerView()

        // API 데이터 로드
        fetchNewReleases()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3열 GridLayout
        newReleasedAdapter = NewReleasedAdapter(emptyList()) { book ->
            // 클릭 이벤트: BookInfoFragment로 이동
            val bookInfoFragment = BookInfoFragment.newInstance(
                book.cover, book.title, book.author, book.publisher, book.pubDate
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, bookInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = newReleasedAdapter
    }

    private fun fetchNewReleases() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch {
            try {
                // 신간 데이터 로드
                val newReleasesResponse = viewModel.fetchNewReleases(apiKey)
                newReleasedAdapter.updateBooks(newReleasesResponse.item) // 어댑터에 데이터 전달
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
