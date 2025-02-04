package com.android.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.ActivityMainBinding
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.android.myapplication.DB.AppDatabase
import com.android.myapplication.DB.Review
import com.android.myapplication.DB.User

// room db 사용하므로 더미 데이터를 앱 실행시 삽입하는 것으로 처리
fun addDummyData(context: Context) {
    val db = AppDatabase.getDatabase(context)
    val userDao = db.userDao()
    val reviewDao = db.reviewDao()

    CoroutineScope(Dispatchers.IO).launch {
        // ✅ 1. 유저 데이터 추가
        val users = listOf(
            User(0, "user1@example.com", "유저1", "https://example.com/user1.jpg"),
            User(0, "user2@example.com", "유저2", "https://example.com/user2.jpg"),
            User(0, "user3@example.com", "유저3", "https://example.com/user3.jpg"),
            User(0, "user4@example.com", "유저4", "https://example.com/user4.jpg"),
            User(0, "user5@example.com", "유저5", "https://example.com/user5.jpg")
        )

        val userIds = users.map { userDao.insertUser(it).toInt() } // 유저 ID 저장

        // ✅ 2. ISBN 목록
        val isbns = listOf(
            "K662930932",  // 소년이 온다
            "K682930444",  // 초역 부처의 말
            "K072638169",  // 삼국지 원전 완역판 세트
            "8954682154",  // 작별하지 않는다
            "8936434594",  // 채식주의자
            "8997780603",  // 조국의 함성
            "K912035329"   // 최태성의 별
        )

        // ✅ 3. 리뷰 내용과 favoriteLine(책 속 문구) 데이터 준비
        val reviewContents = listOf(
            "깊은 감동을 주는 책입니다.",
            "많은 생각을 하게 만드는 책이었습니다.",
            "한 번쯤은 꼭 읽어봐야 할 작품입니다.",
            "스토리와 문장이 아름다운 책입니다.",
            "삶의 의미를 다시 생각하게 해준 책입니다."
        )

        val favoriteLines = listOf(
            "사람은 사람이 끝까지 지켜.", // 소년이 온다
            "진정한 행복은 마음속에서 온다.", // 초역 부처의 말
            "천하는 세 사람이 나누어 가질 것이다.", // 삼국지 원전 완역판 세트
            "우리의 이별은 아직 끝나지 않았다.", // 작별하지 않는다
            "나는 한때 인간이었으나, 지금은 채식주의자다.", // 채식주의자
            "조국의 함성은 결코 사라지지 않는다.", // 조국의 함성
            "역사는 미래를 비추는 거울이다." // 최태성의 별
        )

        // ✅ 4. 리뷰 데이터 추가 (ISBN별 최소 1개 이상 보장)
        val dummyReviews = mutableListOf<Review>()
        var reviewIndex = 0

        for (isbn in isbns) {
            val shuffledUsers = userIds.shuffled().take((1..3).random()) // 각 ISBN당 1~3명의 리뷰 생성
            for (userId in shuffledUsers) {
                val review = Review(
                    reviewId = 0,
                    userId = userId,
                    isbn = isbn,
                    starRate = (3..5).random().toFloat(),
                    review = reviewContents[reviewIndex % reviewContents.size],
                    favoriteLine = favoriteLines[reviewIndex % favoriteLines.size],
                    createdAt = "2025-02-04T12:${(10 * reviewIndex) % 60}:00",
                    likeCount = (0..20).random()
                )
                dummyReviews.add(review)
                reviewIndex++
            }
        }

        // ✅ 5. 중복 검사를 통해 동일한 userId + isbn 조합 방지 후 리뷰 추가
        dummyReviews.forEach { review ->
            val existingReviews = reviewDao.getReviewsByUserAndIsbn(review.userId, review.isbn)
            if (existingReviews.isEmpty()) {
                reviewDao.insertReview(review)
            }
        }
    }
}


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AladinViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addDummyDataIfNeeded()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val bookListFragment: BookListFragment = BookListFragment()
        val archiveFragment: ArchiveFragment = ArchiveFragment()
        val recommendFragment: RecommendFragment = RecommendFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.rootlayout, bookListFragment)
        fragmentTransaction.commit()

        binding.bottomNav.selectedItemId = R.id.bookList

//        // 알라딘 api key 접근법
//        val apiKey = BuildConfig.ALADIN_API_KEY
//        // 알라딘 api 호출 위해 viewModel 객체 받아오기
//        val apiService = RetrofitClient.aladinApi
//        val repository = AladinRepository(apiService)
//        viewModel = AladinViewModel(repository)
//
//        // 아래의 코드를 통해 api 호출 가능
//        lifecycleScope.launch {
//            try {
//                // fetchBestSellers 호출
//                val response = viewModel.fetchBestSellers(apiKey)
//                response.item.forEach { book ->
//                    println("Title: ${book.title}")
//                    println("Author: ${book.author}")
//                    println("Publisher: ${book.publisher}")
//                    println("ISBN: ${book.isbn}")
//                    println("----------")
//                }
//            } catch (e: Exception) {
//                e.printStackTrace() // 에러 처리
//            }
//        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (item.itemId == R.id.bookList) {
                fragmentTransaction.replace(R.id.rootlayout, bookListFragment).commit()
                true
            } else if (item.itemId == R.id.archive) {
                fragmentTransaction.replace(R.id.rootlayout, archiveFragment).commit()
                true
            } else if (item.itemId == R.id.recommend) {
                fragmentTransaction.replace(R.id.rootlayout, recommendFragment).commit()
                true
            } else {
                false
            }
        }
    }

    private fun addDummyDataIfNeeded() {

        CoroutineScope(Dispatchers.IO).launch {
                addDummyData(this@MainActivity)  // 기존에 만든 함수 호출
        }
    }
}