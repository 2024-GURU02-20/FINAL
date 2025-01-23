package com.android.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.android.myapplication.repository.AladinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AladinViewModel(private val repository: AladinRepository) : ViewModel() {

    // 베스트셀러 데이터를 반환하는 suspend 함수
    suspend fun fetchBestSellers(apiKey: String) = withContext(Dispatchers.IO) {
        repository.getBestSellers(apiKey)
    }

    // 신간 데이터를 반환하는 suspend 함수
    suspend fun fetchNewReleases(apiKey: String) = withContext(Dispatchers.IO) {
        repository.getNewReleases(apiKey)
    }

    // 키워드 검색 결과를 반환하는 suspend 함수
    suspend fun searchBooks(apiKey: String, keyword: String) = withContext(Dispatchers.IO) {
        repository.searchByKeyword(apiKey, keyword)
    }
}
