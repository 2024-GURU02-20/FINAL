package com.android.myapplication.DB

import com.android.myapplication.DB.User
import com.android.myapplication.DB.Review
import androidx.room.*
import com.android.myapplication.model.ReviewItem

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

    @Query("SELECT * FROM Review WHERE isbn = :isbn ORDER BY likeCount DESC LIMIT 3")
    suspend fun getReviewsSortedByLikes(isbn: String): List<ReviewItem>

    @Query("SELECT * FROM Review WHERE isbn = :isbn ORDER BY createdAt DESC LIMIT 3")
    suspend fun getReviewsSortedByDate(isbn: String): List<ReviewItem>

    @Query("SELECT * FROM Review WHERE isbn = :isbn ORDER BY starRate DESC LIMIT 3")
    suspend fun getReviewsSortedByRating(isbn: String): List<ReviewItem>


    //은정 추가
    //가장 많은 리뷰를 남긴 유저를 찾고 해당 유저의 ISBN 12개를 가져옴
    //LIMIT 12 → 최대 12개만 가져오도록 설정
    @Query("""
    SELECT isbn FROM review 
    WHERE userId = (SELECT userId FROM review GROUP BY userId ORDER BY COUNT(*) DESC LIMIT 1) 
    LIMIT 12
""")
    suspend fun getTopReaderBooks(): List<String>
}
