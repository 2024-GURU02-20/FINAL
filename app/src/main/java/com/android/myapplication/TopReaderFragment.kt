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
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentTopReaderPickBinding
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class TopReaderFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var topReaderAdapter: MoreInfoAdapter // 기존 MoreInfoAdapter 사용
    private lateinit var viewModel: AladinViewModel

    private lateinit var binding: FragmentTopReaderPickBinding

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
        binding = FragmentTopReaderPickBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뒤로가기
        binding.customTopBar.onBackClick = {
            parentFragmentManager.popBackStack()
        }

        binding.customTopBar.setTitle("다독왕 선정")

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_topreaderList)
        setupRecyclerView()

        // 다독왕 책 목록 불러오기
        fetchTopReaderBooks()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3열 GridLayout
        topReaderAdapter = MoreInfoAdapter(emptyList()) { book ->
            // 책 클릭 시 BookInfoFragment로 이동
            val bookInfoFragment = BookInfoFragment.newInstance(
                book.cover, book.title, book.author, book.publisher, book.pubDate, book.description, book.isbn
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, bookInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = topReaderAdapter
    }

    private fun fetchTopReaderBooks() {
        val database = AppDatabase.getDatabase(requireContext()) // Room DB 인스턴스 가져오기
        val reviewDao = database.reviewDao() // ReviewDao 가져오기

        lifecycleScope.launch {
            try {
                val topReaderIsbnList = reviewDao.getTopReaderBooks() // 가장 많이 읽은 유저의 ISBN 리스트(10개)
                val bookList = mutableListOf<BookItem>()

                for (isbn in topReaderIsbnList) {
                    val response = viewModel.searchBooks(BuildConfig.ALADIN_API_KEY, isbn)
                    if (response.item.isNotEmpty()) {
                        bookList.add(response.item[0]) // 첫 번째 검색 결과 추가
                    }
                }

                topReaderAdapter.updateBooks(bookList) // RecyclerView 업데이트
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
