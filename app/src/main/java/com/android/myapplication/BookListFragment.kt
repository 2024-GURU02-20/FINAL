package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentBookListBinding
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    lateinit var binding: FragmentBookListBinding


    //알라딘 연결 및 객체 참조
    private lateinit var viewModel: AladinViewModel
    lateinit var items: AladinResponse


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // 필요하면 arguments를 처리할 로직 추가
        }


        val apiKey = BuildConfig.ALADIN_API_KEY
        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)
//
//        lifecycleScope.launch {
//            try {
//                val response = viewModel.searchBooks(apiKey, searchQuery ?: "") //이렇게 말고 val response안에 나는 다른 값을 넣어야함  (여기서 나는 searchQuery를 사용하고 있지 않기 때문, 책 표지값으로
//                items = response
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // FragmentBookListBinding을 사용해 XML 레이아웃 연결
        binding = FragmentBookListBinding.inflate(inflater, container, false)





        // 버튼 클릭 이벤트 설정
        setupButtonListeners()

        return binding.root
    }

    private fun setupButtonListeners() {
        // btnMoreinfo1 버튼 클릭 시 BestSellerFragment로 이동
        binding.btnMoreinfo1.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, BestSellerFragment())
                .addToBackStack(null)
                .commit()
        }

        // btnMoreinfo2 버튼 클릭 시 NewReleasedFragment로 이동
        binding.btnMoreinfo2.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, NewReleasedFragment())
                .addToBackStack(null)
                .commit()
        }

        // btnMoreinfo3 버튼 클릭 시 TopReaderPickFragment로 이동
        binding.btnMoreinfo3.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootlayout, TopReaderPickFragment())
                .addToBackStack(null)
                .commit()
        }

        // 추가적인 검색 버튼 클릭 이벤트 (검색 이벤트 추가)
        binding.search.setOnClickListener {
            // 검색 버튼 클릭 시 처리할 내용 추가
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = BookListFragment().apply {
            arguments = Bundle().apply {
                // 필요한 인자를 여기에 추가
            }
        }
    }
}
