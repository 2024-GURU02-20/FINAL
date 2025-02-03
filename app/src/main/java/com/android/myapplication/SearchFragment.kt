package com.android.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.android.myapplication.databinding.FragmentSearchBinding
import com.android.myapplication.databinding.FragmentSearchResultBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.isNotBlank()) { // 값이 비어있지 않은 경우만 이동
                        val bundle = Bundle().apply {
                            putString("search_query", it)
                        }

                        // SearchResultFragment로 이동
                        val searchResultFragment = SearchResultFragment().apply {
                            arguments = bundle
                        }

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.rootlayout, searchResultFragment)
                            .addToBackStack(null) // 뒤로 가기 스택 추가
                            .commit()
                    } else {
                        binding.search.setQuery("", false)
                        binding.search.clearFocus()
                        binding.search.isIconified = false
                        binding.search.queryHint = "검색어를 입력해주세요."
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 검색어 변경 이벤트 처리 (필요한 경우)
                return false
            }
        })
    }

    companion object {
        fun newInstance(data: String): SearchFragment {
            val fragment = SearchFragment()
            val bundle = Bundle()
            bundle.putString("data_key", data)
            fragment.arguments = bundle
            return fragment
        }
    }
}