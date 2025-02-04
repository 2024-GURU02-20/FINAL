package com.android.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentRecommendBinding
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RecommendFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recommendAdapter: RecommendAdapter
    private lateinit var viewModel: AladinViewModel

    private lateinit var binding: FragmentRecommendBinding

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
    ): View {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)

        val user = FirebaseAuth.getInstance().currentUser
        binding.customProfileView.setOnClickListener {
            if (user == null) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d("로그인 확인", user.photoUrl.toString())
            binding.customProfileView.setData("추천받으세요!", "원하는 책을", user.photoUrl)
        }

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recycler_bestseller)
        setupRecyclerView()

        // API에서 추천 도서 불러오기
        fetchRecommendedBooks()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3) // 3열 GridLayout 사용
        recommendAdapter = RecommendAdapter(emptyList()) { book ->
            val bookInfoFragment = BookInfoFragment.newInstance(
                book.cover, book.title, book.author, book.publisher, book.pubDate, book.description, book.isbn
            )
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, bookInfoFragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = recommendAdapter
    }

    private fun fetchRecommendedBooks() {
        val apiKey = BuildConfig.ALADIN_API_KEY
        lifecycleScope.launch {
            try {
                // API에서 베스트셀러 데이터 가져와서 추천 목록으로 사용
                val recommendedBooksResponse = viewModel.fetchBestSellers(apiKey)

                // 최대 9개까지만 표시
                val limitedBooks = recommendedBooksResponse.item.take(9)

                recommendAdapter.updateBooks(limitedBooks) // 어댑터에 데이터 전달
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
