package com.android.myapplication.repository

import com.android.myapplication.api.AladinApiService

class AladinRepository(private val apiService: AladinApiService) {

    suspend fun getBestSellers(apiKey: String) = apiService.getBestSellers(apiKey)

    suspend fun getNewReleases(apiKey: String) = apiService.getNewReleases(apiKey)

    suspend fun searchByKeyword(apiKey: String, keyword: String) = apiService.searchByKeyword(apiKey, keyword)
}
