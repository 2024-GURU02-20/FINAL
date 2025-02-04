package com.android.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.DB.Review
import com.android.myapplication.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(private val repository: ReviewRepository) : ViewModel() {

    private val _selectedBookReview = MutableStateFlow<Review?>(null) // ✅ Review 단일 객체
    val selectedBookReview = _selectedBookReview.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews = _reviews.asStateFlow()

    private val _starRate = MutableStateFlow(0f)
    val starRate = _starRate.asStateFlow()

    private val _likeCount = MutableStateFlow(0)
    val likeCount = _likeCount.asStateFlow()

    fun fetchReviewsByIsbn(isbn: String) {
        viewModelScope.launch {
            _reviews.value = repository.getReviewsByIsbn(isbn)
        }
    }

    fun addReview(review: Review) {
        viewModelScope.launch {
            repository.insertReview(review)
            fetchReviewsByIsbn(review.isbn)  // 저장 후 해당 책의 리뷰 다시 가져오기
        }
    }

    fun fetchStarRate(reviewId: Int) {
        viewModelScope.launch {
            _starRate.value = repository.getStarRate(reviewId)
        }
    }

    fun fetchLikeCount(reviewId: Int) {
        viewModelScope.launch {
            _likeCount.value = repository.getLikeCount(reviewId)
        }
    }

    fun updateStarRate(reviewId: Int, newStarRate: Float) {
        viewModelScope.launch {
            repository.updateStarRate(reviewId, newStarRate)
            fetchStarRate(reviewId)  // 업데이트 후 값 반영
        }
    }

    fun updateLikeCount(reviewId: Int, newLikeCount: Int) {
        viewModelScope.launch {
            repository.updateLikeCount(reviewId, newLikeCount)
            fetchLikeCount(reviewId)  // 업데이트 후 값 반영
        }
    }
}
