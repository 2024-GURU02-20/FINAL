package com.android.myapplication.model

data class ReviewItem(
    val review: String,  // 리뷰 내용
    val starRate: Float,        // 별점
    val like: Int        // 좋아요 개수
)
