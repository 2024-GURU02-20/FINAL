package com.android.myapplication.DB

import com.android.myapplication.DB.User
import com.android.myapplication.DB.Review
import androidx.room.*

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUserById(id: Int): User?

    @Query("SELECT * FROM review WHERE userId = :userId")
    suspend fun getReviewsByUserId(userId: Int): List<Review>

    // isbn 사용
    @Query("SELECT isbn FROM review WHERE userId = :userId")
    suspend fun getIsbnListByUserId(userId: Int): List<String>

    @Query("DELETE FROM user WHERE userId = :id")
    suspend fun deleteUserById(id: Int)

    @Query("DELETE FROM review WHERE reviewId = :id")
    suspend fun deleteReviewById(id: Int)
}
