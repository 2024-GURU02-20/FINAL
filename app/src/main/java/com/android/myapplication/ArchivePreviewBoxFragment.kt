package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.android.myapplication.databinding.FragmentArchivePreviewBoxBinding
import com.android.myapplication.databinding.FragmentArchiveReviewBinding
import com.android.myapplication.ArchiveReviewFragment.DayViewContainer
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.DB.User
import com.android.myapplication.repository.ReviewRepository
import com.android.myapplication.viewmodel.ReviewViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ArchivePreviewBoxFragment : Fragment() {
    private lateinit var binding: FragmentArchivePreviewBoxBinding
    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var repository: ReviewRepository

    private val selectedIsbn = "9781234567890" // 사용자가 선택한 책의 ISBN (테스트용)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentArchivePreviewBoxBinding.inflate(inflater, container, false)

        val database = AppDatabase.getDatabase(requireContext())
        repository = ReviewRepository(database.reviewDao())

        // 사용자가 선택한 책의 리뷰 가져오기
        viewModel.fetchReviewsByIsbn(selectedIsbn)

        // UI 업데이트 (선택한 책의 리뷰 반영)
        lifecycleScope.launch {
            viewModel.selectedBookReview.collect { review ->
                if (review != null) {
                    binding.bookDetail.text = review.review // 사용자가 남긴 리뷰 표시
                    binding.favoriteLine.text = "마음에 드는 구절: ${review.favoriteLine}" // 마음에 드는 한 줄 추가

                    // ✅ 별점 숫자 업데이트
                    binding.tvStarRate.text = review.starRate.toString()

                    // ✅ 추천 수 숫자 업데이트
                    binding.tvLikeCount.text = review.likeCount.toString()
                } else {
                    binding.bookDetail.text = "등록된 리뷰 없음"
                    binding.favoriteLine.text = "" // 등록된 구절 없음

                    binding.tvStarRate.text = "0.0"
                    binding.tvLikeCount.text = "0"

                }
            }
        }

        // ✅ 아카이빙 메인 화면으로 이동
        binding.scrollTool.setOnClickListener {
            requireActivity().runOnUiThread {
                val currentFragment = parentFragmentManager.findFragmentById(R.id.preview_box)
                if (currentFragment !is ArchiveFragment) { // 중복 방지
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.rootlayout, ArchiveFragment()) // 아카이빙 메인 화면으로
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        // ✅ 수정하기 버튼 클릭 -> 수정 완료! Toast 메시지 적용
        binding.modifyButton.setOnClickListener {
            Toast.makeText(requireContext(), "수정 완료!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}