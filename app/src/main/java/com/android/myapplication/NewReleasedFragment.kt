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
import com.android.myapplication.databinding.FragmentNewReleasedBinding
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class NewReleasedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var moreInfoAdapter: MoreInfoAdapter
    private lateinit var viewModel: AladinViewModel

    private lateinit var binding: FragmentNewReleasedBinding

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

        binding = FragmentNewReleasedBinding.inflate(inflater, container, false)
        binding.customTopBar.onBackClick = {
            parentFragmentManager.popBackStack()
        }
        binding.customTopBar.setTitle("신간")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_newreleasedList)
        setupRecyclerView()

        // API 호출 및 데이터 로드
        fetchNewReleases()
    }

    // RecyclerView를 초기화하는 함수


    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        // moreinfoAdapter 초기화
        moreInfoAdapter = MoreInfoAdapter(emptyList()) { book ->
            // 책 클릭 시 BookInfoFragment로 이동 (책 정보를 전달)
            val bookInfoFragment = BookInfoFragment.newInstance(
                book.cover, book.title, book.author, book.publisher, book.pubDate, book.description
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, bookInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = moreInfoAdapter
    }

    // API에서 신간 데이터를 가져와 RecyclerView에 업데이트하는 함수
    private fun fetchNewReleases() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch {
            try {
                // API 호출하여 신간 데이터 가져오기
                val newReleasesResponse = viewModel.fetchNewReleases(apiKey)
                moreInfoAdapter.updateBooks(newReleasesResponse.item) // 어댑터에 데이터 전달
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
