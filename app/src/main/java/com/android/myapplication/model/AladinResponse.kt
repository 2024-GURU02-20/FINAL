package com.android.myapplication.model

data class AladinResponse(
    val version: String,
    val title: String,
    val link: String,
    val pubDate: String,
    val totalResults: Int,
    val startIndex: Int,
    val itemsPerPage: Int,
    val query: String,
    val searchCategoryId: Int,
    val searchCategoryName: String,
    val item: List<BookItem>
)

