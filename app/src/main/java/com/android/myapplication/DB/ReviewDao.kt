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


    //은정 추가
    //가장 많은 리뷰를 남긴 유저를 찾고 해당 유저의 ISBN 12개를 가져옴
    //LIMIT 12 → 최대 12개만 가져오도록 설정
    @Query("""
    SELECT isbn FROM review 
    WHERE userId = (SELECT userId FROM review GROUP BY userId ORDER BY COUNT(*) DESC LIMIT 1) 
    LIMIT 12
""")
    suspend fun getTopReaderBooks(): List<String>

    //추천 개수가 높은 순서대로 정렬하여 상위 3개의 리뷰만 가져오기
    @Query("SELECT * FROM review ORDER BY `like` DESC LIMIT 3")
    suspend fun getTopLikedReviews(): List<Review>

    // 해당 리뷰의 추천수 1증가 (추천 버튼 클릭 시)
    @Query("UPDATE review SET `like` = `like` + 1 WHERE reviewId = :reviewId")
    suspend fun increaseReviewLike(reviewId: Int)


}
