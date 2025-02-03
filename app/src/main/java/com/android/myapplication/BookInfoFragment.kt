package com.android.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.myapplication.databinding.FragmentBookInfoBinding
import com.bumptech.glide.Glide

class BookInfoFragment : Fragment() {

    // 책 정보를 저장할 변수 선언 (Fragment 간 데이터 전달을 위해 사용)
    private lateinit var coverUrl: String  // 책 표지 URL
    private lateinit var title: String  // 책 제목
    private lateinit var author: String  // 책 저자
    private lateinit var publisher: String  // 출판사
    private lateinit var pubDate: String  // 발행일
    private lateinit var description: String  // 책 소개

    private lateinit var binding: FragmentBookInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 전달받은 데이터를 arguments에서 꺼내 변수에 저장
        arguments?.let {
            coverUrl = it.getString(ARG_COVER_URL) ?: "" // 기본값은 빈 문자열
            title = it.getString(ARG_TITLE) ?: ""
            author = it.getString(ARG_AUTHOR) ?: ""
            publisher = it.getString(ARG_PUBLISHER) ?: ""
            pubDate = it.getString(ARG_PUB_DATE) ?: ""
            description = it.getString(ARG_DESCRIPTION) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookInfoBinding.inflate(inflater, container, false)

        binding.customTopBar.onBackClick = {
            parentFragmentManager.popBackStack()
        }
        binding.customTopBar.setTitle("")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // XML 레이아웃의 UI 요소들을 가져와 변수에 저장
//        val bookMainTitleTextView: TextView = view.findViewById(R.id.info_book_maintitle) // 상단 제목
        val bookCoverImageView: ImageView = view.findViewById(R.id.info_book_cover) // 책 표지
        val bookTitleTextView: TextView = view.findViewById(R.id.info_book_title) // 책 제목
        val bookAuthorTextView: TextView = view.findViewById(R.id.info_book_author) // 저자
        val bookPublisherTextView: TextView = view.findViewById(R.id.info_book_publisher) // 출판사
        val bookPubDateTextView: TextView = view.findViewById(R.id.info_book_pub_date) // 발행일
        val bookDescriptionTextView: TextView = view.findViewById(R.id.info_book_description) // 책 소개

        // 데이터 UI에 적용 (Glide를 사용해 책 표지 이미지 설정)
//        bookMainTitleTextView.text = title
        Glide.with(requireContext()).load(coverUrl).into(bookCoverImageView) // 책 표지 이미지 로드
        bookTitleTextView.text = title // 책 제목
        bookAuthorTextView.text = author // 저자
        bookPublisherTextView.text = publisher // 출판사
        bookPubDateTextView.text = pubDate // 발행일
        bookDescriptionTextView.text = if (description.isNotBlank()) description else "." // 책 소개 (없을 경우 기본 문구 표시 )

    }

    companion object {
        // 키 값 정의 (데이터 전달을 위한 변수 이름 지정)
        private const val ARG_COVER_URL = "cover_url"
        private const val ARG_TITLE = "title"
        private const val ARG_AUTHOR = "author"
        private const val ARG_PUBLISHER = "publisher"
        private const val ARG_PUB_DATE = "pub_date"
        private const val ARG_DESCRIPTION = "description"

        // BookInfoFragment 인스턴스를 생성하는 메서드 (Fragment에 데이터 전달)
        fun newInstance(
            coverUrl: String, title: String, author: String, publisher: String,
            pubDate: String, description: String
        ) = BookInfoFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_COVER_URL, coverUrl) // 책 표지 URL 저장
                putString(ARG_TITLE, title) // 책 제목 저장
                putString(ARG_AUTHOR, author) // 저자 저장
                putString(ARG_PUBLISHER, publisher) // 출판사 저장
                putString(ARG_PUB_DATE, pubDate) // 발행일 저장
                putString(ARG_DESCRIPTION, description) // 책 소개 저장
            }
        }
    }
}
