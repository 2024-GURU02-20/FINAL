package com.android.myapplication.api

import com.android.myapplication.model.AladinResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AladinApiService {

    // 베스트셀러 API
    @GET("ItemList.aspx")
    suspend fun getBestSellers(
        @Query("ttbkey") apiKey: String,
        @Query("QueryType") queryType: String = "Bestseller",
        @Query("MaxResults") maxResults: Int = 10,
        @Query("SearchTarget") searchTarget: String = "Book",
        @Query("Output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): AladinResponse

    // 신간 API
    @GET("ItemList.aspx")
    suspend fun getNewReleases(
        @Query("ttbkey") apiKey: String,
        @Query("QueryType") queryType: String = "ItemNewAll",
        @Query("MaxResults") maxResults: Int = 10,
        @Query("SearchTarget") searchTarget: String = "Book",
        @Query("Output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): AladinResponse

    // 키워드 검색 API
    @GET("ItemSearch.aspx")
    suspend fun searchByKeyword(
        @Query("ttbkey") apiKey: String,
        @Query("Query") keyword: String,
        @Query("MaxResults") maxResults: Int = 10,
        @Query("SearchTarget") searchTarget: String = "Book",
        @Query("Output") output: String = "js",
        @Query("Version") version: String = "20131101"
    ): AladinResponse
}