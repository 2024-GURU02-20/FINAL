package com.android.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myapplication.repository.AladinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AladinViewModel(private val repository: AladinRepository) : ViewModel() {

    fun fetchBestSellers(apiKey: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getBestSellers(apiKey)
            }
            // Handle response (e.g., update UI state)
        }
    }

    fun fetchNewReleases(apiKey: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.getNewReleases(apiKey)
            }
            // Handle response (e.g., update UI state)
        }
    }

    fun searchBooks(apiKey: String, keyword: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                repository.searchByKeyword(apiKey, keyword)
            }
            // Handle response (e.g., update UI state)
        }
    }
}