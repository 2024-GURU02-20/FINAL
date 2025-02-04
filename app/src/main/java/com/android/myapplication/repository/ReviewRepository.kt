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

    suspend fun getReviewByIsbn(isbn: String): List<Review> {
        return reviewDao.getReviewsByIsbn(isbn)
    }

    suspend fun getReviewsByUserId(userId: Int): List<Review> {
        return reviewDao.getReviewsByUserId(userId)
    }

    suspend fun getIsbnListByUserId(userId: Int): List<String> {
        return reviewDao.getIsbnListByUserId(userId)
    }

    suspend fun getReviewsByIsbn(isbn: String): List<Review> {
        return reviewDao.getReviewsByIsbn(isbn)
    }

    suspend fun getStarRate(reviewId: Int): Float {
        return reviewDao.getStarRate(reviewId)
    }

    suspend fun getLikeCount(reviewId: Int): Int {
        return reviewDao.getLikeCount(reviewId)
    }

    suspend fun updateStarRate(reviewId: Int, newStarRate: Float) {
        reviewDao.updateStarRate(reviewId, newStarRate)
    }

    suspend fun updateLikeCount(reviewId: Int, newLikeCount: Int) {
        reviewDao.updateLikeCount(reviewId, newLikeCount)
    }
}