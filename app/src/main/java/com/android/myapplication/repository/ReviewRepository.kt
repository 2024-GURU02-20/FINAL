package com.android.myapplication.repository

import com.android.myapplication.DB.ReviewDao
import com.android.myapplication.DB.User
import com.android.myapplication.DB.Review

class ReviewRepository(private val reviewDao: ReviewDao) {
    suspend fun insertUser(user: User): Long {
        return reviewDao.insertUser(user)
    }

    suspend fun insertReview(review: Review): Long {
        return reviewDao.insertReview(review)
    }

    suspend fun getReviewsByUserId(userId: Int): List<Review> {
        return reviewDao.getReviewsByUserId(userId)
    }

    suspend fun getIsbnListByUserId(userId: Int): List<String> {
        return reviewDao.getIsbnListByUserId(userId)
    }
}