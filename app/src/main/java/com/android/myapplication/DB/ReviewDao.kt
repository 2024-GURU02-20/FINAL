package com.android.myapplication.DB

import com.android.myapplication.DB.User
import com.android.myapplication.DB.Review
import androidx.room.*
import com.android.myapplication.model.ReviewItem
import java.util.concurrent.Flow

@Dao
interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review): Long

    @Query("SELECT * FROM user WHERE userId = :id")
    suspend fun getUserById(id: Int): User?

    // 특정 유저의 모든 리뷰 가져오기
    @Query("SELECT * FROM review WHERE userId = :userId")
    suspend fun getReviewsByUserId(userId: Int): List<Review>

    // 특정 리뷰의 별점 가져오기 (실시간 업데이트)
    @Query("SELECT starRate FROM review WHERE reviewId = :reviewId")
    fun getStarRate(reviewId: Int): Float

    // 특정 리뷰의 추천 수 가져오기 (실시간 업데이트)
    @Query("SELECT `like` FROM review WHERE reviewId = :reviewId")
    suspend fun getLikeCount(reviewId: Int): Int

    // isbn 사용
    @Query("SELECT isbn FROM review WHERE userId = :userId")
    suspend fun getIsbnListByUserId(userId: Int): List<String>

    // 특정 책(ISBN)의 리뷰 가져오기 (최신순, 별점 높은 순, 추천 수 높은 순)
    @Query("SELECT * FROM review WHERE isbn = :isbn ORDER BY createdAt DESC, starRate DESC, `like` DESC")
    suspend fun getReviewsByIsbn(isbn: String): List<Review>

    @Query("DELETE FROM user WHERE userId = :id")
    suspend fun deleteUserById(id: Int)

    @Query("DELETE FROM review WHERE reviewId = :id")
    suspend fun deleteReviewById(id: Int)

    @Query("SELECT * FROM Review WHERE isbn = :isbn ORDER BY `like` DESC LIMIT 3")
    suspend fun getReviewsSortedByLikes(isbn: String): List<ReviewItem>

    @Query("SELECT * FROM Review WHERE isbn = :isbn ORDER BY createdAt DESC LIMIT 3")
    suspend fun getReviewsSortedByDate(isbn: String): List<ReviewItem>

    @Query("SELECT * FROM Review WHERE isbn = :isbn ORDER BY starRate DESC LIMIT 3")
    suspend fun getReviewsSortedByRating(isbn: String): List<ReviewItem>

    // 별점 업데이트
    @Query("UPDATE review SET starRate = :newStarRate WHERE reviewId = :reviewId")
    suspend fun updateStarRate(reviewId: Int, newStarRate: Float)

    // 추천 수(좋아요) 업데이트
    @Query("UPDATE review SET `like` = :newLikeCount WHERE reviewId = :reviewId")
    suspend fun updateLikeCount(reviewId: Int, newLikeCount: Int)

    @Query("SELECT favoriteLine FROM review WHERE isbn = :isbn LIMIT 3")
    suspend fun getFavoriteLinesByIsbn(isbn: String): List<String>

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

    @Query("SELECT * FROM review WHERE userId = :userId AND isbn = :isbn LIMIT 1")
    suspend fun getReviewsByUserAndIsbn(userId: Int, isbn: String): List<Review>
//
//    // 해당 리뷰의 추천수 1증가 (추천 버튼 클릭 시)
//    @Query("UPDATE review SET `like` = `like` + 1 WHERE reviewId = :reviewId")
//    suspend fun increaseReviewLike(reviewId: Int)

}
