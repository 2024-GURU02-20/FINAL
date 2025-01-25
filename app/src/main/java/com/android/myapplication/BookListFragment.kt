package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment



///레트로핏 : 동작 시키게 해주는 라이브러리
/// 베스트 함수 각각 짜놓음
// 알라딘 형태 (타입) , 북아이테 ㅁ(타입

//레포지르트 함수 연결
//



//        // 알라딘 api key 접근법
//        val apiKey = BuildConfig.ALADIN_API_KEY
//        // 알라딘 api 호출 위해 viewModel 객체 받아오기
//        val apiService = RetrofitClient.aladinApi
//        val repository = AladinRepository(apiService)
//        viewModel = AladinViewModel(repository)
///초기화

//////        // 아래의 코드를 통해 api 호출 가능
////        lifecycleScope.launch {
////            try {
////                // fetchBestSellers 호출
////                val response = viewModel.fetchBestSellers(apiKey)
////                response.item.forEach { book ->
////                    println("Title: ${book.title}")
////                    println("Author: ${book.author}")
////                    println("Publisher: ${book.publisher}")
////                    println("ISBN: ${book.isbn}")
////                    println("----------")
////                }
////            } catch (e: Exception) {
////                e.printStackTrace() // 에러 처리
////            }
////        }



///class AladinViewModel(private val repository: AladinRepository) : ViewModel() { // 클래스 불러와서
//
//    // 베스트셀러 데이터를 반환하는 suspend 함수
//    suspend fun fetchBestSellers(apiKey: String) = withContext(Dispatchers.IO) {
//        repository.getBestSellers(apiKey)
//    }
//
//    // 신간 데이터를 반환하는 suspend 함수
//    suspend fun fetchNewReleases(apiKey: String) = withContext(Dispatchers.IO) {
//        repository.getNewReleases(apiKey)
//    }
//
//    // 키워드 검색 결과를 반환하는 suspend 함수
//    suspend fun searchBooks(apiKey: String, keyword: String) = withContext(Dispatchers.IO) {
//        repository.searchByKeyword(apiKey, keyword)
//    }
//}


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


class BookListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃 연결
        return inflater.inflate(R.layout.fragment_book_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // btnMoreinfo1 버튼의 클릭 이벤트 설정
        val btnMoreinfo1: Button = view.findViewById(R.id.btnMoreinfo1)
        btnMoreinfo1.setOnClickListener {
            // BestSellerFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, BestSellerFragment()) // rootlayout은 activity_main.xml의 FrameLayout ID
                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기
                .commit()
        }

        // btnMoreinfo2 버튼의 클릭 이벤트 설정
        val btnMoreinfo2: Button = view.findViewById(R.id.btnMoreinfo2)
        btnMoreinfo2.setOnClickListener {
            // BestSellerFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, NewReleasedFragment()) // rootlayout은 activity_main.xml의 FrameLayout ID
                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기
                .commit()
        }
        // btnMoreinfo3 버튼의 클릭 이벤트 설정
        val btnMoreinfo3: Button = view.findViewById(R.id.btnMoreinfo3)
        btnMoreinfo3.setOnClickListener {

            // BestSellerFragment로 전환
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, TopReaderPickFragment()) // rootlayout은 activity_main.xml의 FrameLayout ID
                .addToBackStack(null) // 뒤로 가기 시 이전 Fragment로 돌아가기
                .commit()
        }
    }
}
