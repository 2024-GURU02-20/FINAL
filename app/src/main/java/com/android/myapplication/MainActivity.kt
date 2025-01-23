package com.android.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AladinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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
}