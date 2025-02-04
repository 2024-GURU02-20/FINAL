package com.android.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ✅ 기존 ViewModel에서 `repository`를 생성자로 받지 않고 내부에서 초기화
class ReviewViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReviewRepository

    init {
        // ✅ 내부에서 Repository를 초기화
        val reviewDao = AppDatabase.getDatabase(application).reviewDao()
        repository = ReviewRepository(reviewDao)
    }

    private val _selectedBookReview = MutableStateFlow<Review?>(null)
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
            fetchReviewsByIsbn(review.isbn)
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
            fetchStarRate(reviewId)
        }
    }

    fun updateLikeCount(reviewId: Int, newLikeCount: Int) {
        viewModelScope.launch {
            repository.updateLikeCount(reviewId, newLikeCount)
            fetchLikeCount(reviewId)
        }
    }
}
