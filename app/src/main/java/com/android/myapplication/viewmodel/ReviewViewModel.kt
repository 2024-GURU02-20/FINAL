package com.android.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.android.myapplication.DB.Review
import com.android.myapplication.DB.User
import com.android.myapplication.api.AladinApiService
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReviewViewModel(private val repository: ReviewRepository, private val apiService: AladinApiService) : ViewModel() {

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun insertReview(review: Review) = viewModelScope.launch {
        repository.insertReview(review)
    }

    fun loadReviews(userId: Int) = viewModelScope.launch {
        _reviews.postValue(repository.getReviewsByUserId(userId))
    }

    private val _books = MutableLiveData<List<AladinResponse>>()
    val books: LiveData<List<AladinResponse>> get() = _books

    fun loadBooksByUser(userId: Int) = viewModelScope.launch {
        try {
            // 1. DB에서 isbn 리스트 가져오기
            val isbnList = withContext(Dispatchers.IO) {
                repository.getIsbnListByUserId(userId)
            }

            // 2. API에서 책 정보 가져오기 (비동기 병렬 실행)
            val bookList = isbnList.map { isbn ->
                async(Dispatchers.IO) { apiService.searchByKeyword(keyword = isbn, apiKey = "YOUR_ALADIN_API_KEY") }
            }.awaitAll()  // 모든 API 요청이 완료될 때까지 대기

            // 3. UI 업데이트
            _books.postValue(bookList)

        } catch (e: Exception) { //  만약 코드 실행 중 오류가 발생하면 catch 블록으로 이동
            Log.e("ReviewViewModel", "Error loading books: ${e.message}") // 오류 로그 출력(Logcat에서 확인)
        }
    }
}
