package com.android.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class BookInfoFragment : Fragment() {

    private lateinit var coverUrl: String
    private lateinit var title: String
    private lateinit var author: String
    private lateinit var publisher: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            coverUrl = it.getString(ARG_COVER_URL) ?: ""
            title = it.getString(ARG_TITLE) ?: ""
            author = it.getString(ARG_AUTHOR) ?: ""
            publisher = it.getString(ARG_PUBLISHER) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI에 데이터 바인딩
        val bookCoverImageView: ImageView = view.findViewById(R.id.info_book_cover)
        val bookTitleTextView: TextView = view.findViewById(R.id.info_book_title)
        val bookAuthorTextView: TextView = view.findViewById(R.id.info_book_author)
        val bookPublisherTextView: TextView = view.findViewById(R.id.info_book_publisher)

        // 책 데이터 표시
        Glide.with(requireContext()).load(coverUrl).into(bookCoverImageView)
        bookTitleTextView.text = title
        bookAuthorTextView.text = author
        bookPublisherTextView.text = publisher
    }

    companion object {
        private const val ARG_COVER_URL = "cover_url"
        private const val ARG_TITLE = "title"
        private const val ARG_AUTHOR = "author"
        private const val ARG_PUBLISHER = "publisher"

        // BookInfoFragment 생성 메서드
        fun newInstance(coverUrl: String, title: String, author: String, publisher: String) =
            BookInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COVER_URL, coverUrl)
                    putString(ARG_TITLE, title)
                    putString(ARG_AUTHOR, author)
                    putString(ARG_PUBLISHER, publisher)
                }
            }
    }
}