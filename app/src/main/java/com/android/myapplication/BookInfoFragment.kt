package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.ReviewDao
import com.android.myapplication.databinding.FragmentBookInfoBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class BookInfoFragment : Fragment() {

    private lateinit var binding: FragmentBookInfoBinding

    // 책 정보를 저장할 변수 선언
    private lateinit var coverUrl: String
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var publisher: String
    private lateinit var pubDate: String
    private lateinit var description: String
    private lateinit var isbn: String

    private lateinit var reviewAdapter: BookinfoReviewRecyclerViewAdapter
    private lateinit var reviewDao: ReviewDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // arguments에서 데이터 가져오기
        arguments?.let {
            coverUrl = it.getString(ARG_COVER_URL) ?: ""
            title = it.getString(ARG_TITLE) ?: ""
            author = it.getString(ARG_AUTHOR) ?: ""
            publisher = it.getString(ARG_PUBLISHER) ?: ""
            pubDate = it.getString(ARG_PUB_DATE) ?: ""
            description = it.getString(ARG_DESCRIPTION) ?: ""
            isbn = it.getString(ARG_ISBN) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookInfoBinding.inflate(inflater, container, false)

        // 뒤로 가기 버튼 설정
        binding.customTopBar.onBackClick = {
            parentFragmentManager.popBackStack()
        }
        binding.customTopBar.setTitle("")

        reviewDao = AppDatabase.getDatabase(requireContext()).reviewDao()

        setupRecyclerView()
        setupSortButtons()
        loadReviews("추천순")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 요소에 데이터 적용
        Glide.with(requireContext()).load(coverUrl).into(binding.infoBookCover) // 책 표지 이미지
        Glide.with(requireContext()) // 배경 이미지
            .load(coverUrl)
            .override((binding.infoBookBackground.width * 1.5).toInt(), (binding.infoBookBackground.height * 1.5).toInt()) // 50% 확대
            .centerCrop() // 중앙 크롭
            .into(binding.infoBookBackground)

        // 알파값 50% 적용 (0 ~ 255 범위)
        binding.infoBookBackground.imageAlpha = 127

        binding.infoBookTitle.text = title // 책 제목
        binding.infoBookAuthor.text = author // 저자
        binding.infoBookDescription.text = if (description.isNotBlank()) description else "." // 책 소개
        val publisherDate: String = "$publisher • $pubDate"
        binding.infoBookPublisherDate.text = publisherDate
    }

    private fun setupRecyclerView() {
        reviewAdapter = BookinfoReviewRecyclerViewAdapter(emptyList())
        binding.reviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.reviewRecyclerView.adapter = reviewAdapter
    }

    private fun loadReviews(sortType: String) {
        lifecycleScope.launch {
            val reviews = when (sortType) {
                "최신순" -> reviewDao.getReviewsSortedByDate(isbn)
                "별점순" -> reviewDao.getReviewsSortedByRating(isbn)
                else -> reviewDao.getReviewsSortedByLikes(isbn) // 기본값: 추천순
            }
            reviewAdapter.updateReviews(reviews)
        }
    }

    private fun setupSortButtons() {
        binding.btnRecommend.setOnClickListener { loadReviews("추천순") }
        binding.btnLatest.setOnClickListener { loadReviews("최신순") }
        binding.btnRating.setOnClickListener { loadReviews("별점순") }
    }

    companion object {
        // 데이터 키 값
        private const val ARG_COVER_URL = "cover_url"
        private const val ARG_TITLE = "title"
        private const val ARG_AUTHOR = "author"
        private const val ARG_PUBLISHER = "publisher"
        private const val ARG_PUB_DATE = "pub_date"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_ISBN = "isbn"

        // BookInfoFragment 인스턴스 생성 메서드
        fun newInstance(
            coverUrl: String, title: String, author: String, publisher: String,
            pubDate: String, description: String, isbn: String
        ) = BookInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_COVER_URL, coverUrl)
                putString(ARG_TITLE, title)
                putString(ARG_AUTHOR, author)
                putString(ARG_PUBLISHER, publisher)
                putString(ARG_PUB_DATE, pubDate)
                putString(ARG_DESCRIPTION, description)
                putString(ARG_ISBN, isbn)
            }
        }
    }
}
