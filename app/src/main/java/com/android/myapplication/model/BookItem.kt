package com.android.myapplication.model

data class BookItem(
    val title: String,           // 책 제목
    val author: String,          // 저자
    val publisher: String,       // 출판사
    val pubDate: String,         // 출간일
    val description: String,     // 책 설명
    val link: String,            // 책 상세 페이지 링크
    val isbn: String             // ISBN 값
)
