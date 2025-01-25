package com.android.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.android.myapplication.api.RetrofitClient
import com.android.myapplication.databinding.FragmentSearchResultBinding
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.model.BookItem
import com.android.myapplication.repository.AladinRepository
import com.android.myapplication.viewmodel.AladinViewModel
import kotlinx.coroutines.launch

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding
    private lateinit var viewModel: AladinViewModel
    lateinit var items: AladinResponse


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        val searchQuery = arguments?.getString("search_query")

        binding.search.setQuery(searchQuery, false)

        val apiKey = BuildConfig.ALADIN_API_KEY
        val apiService = RetrofitClient.aladinApi
        val repository = AladinRepository(apiService)
        viewModel = AladinViewModel(repository)

        lifecycleScope.launch {
            try {
                val response = viewModel.searchBooks(apiKey, searchQuery ?: "")
                items = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        val layoutManager = GridLayoutManager(context, 2)
        binding.searchResultRecyclerView.setLayoutManager(layoutManager)
        val adapter: SearchResultBookAdapter = SearchResultBookAdapter(context, items)
        binding.searchResultRecyclerView.setAdapter(adapter)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}