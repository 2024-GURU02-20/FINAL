package com.android.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide

// 베스트셀러 & 신간 리스트에서 공통으로 사용하는 어댑터
class BookListAdapter(
    private var items: List<BookItem>, // 책 리스트
    private val onItemClick: (BookItem) -> Unit // 클릭 이벤트 콜백 함수
) : RecyclerView.Adapter<BookListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.bl_book_cover) // 책 표지
        val bookTitleTextView: TextView = itemView.findViewById(R.id.bl_book_title) // 책 제목
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.bl_book_author) // 책 저자
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booklist_book_recyclerview, parent, false) // 공통 XML 사용
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]

        // 책 제목 및 저자 설정
        holder.bookTitleTextView.text = book.title
        holder.bookAuthorTextView.text = book.author

        // Glide를 사용해 책 표지 로드
        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)

        // 책 클릭 시 BookInfoFragment로 이동
        holder.itemView.findViewById<LinearLayout>(R.id.booklist_bookcover).setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int = items.size

    // 새로운 데이터를 받아 RecyclerView를 갱신하는 메서드
    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
