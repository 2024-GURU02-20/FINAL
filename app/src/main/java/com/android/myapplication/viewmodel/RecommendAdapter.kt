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

class RecommendAdapter(
    private var items: List<BookItem>,
    private val onItemClick: (BookItem) -> Unit
) : RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.rec_book_cover) // 책 표지
        val bookTitleTextView: TextView = itemView.findViewById(R.id.rec_book_title) // 책 제목
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.rec_book_author) // 책 저자
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommend_book_recyclerview, parent, false)
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

        // 책 클릭 이벤트 설정
        holder.itemView.findViewById<LinearLayout>(R.id.recommend_book_item).setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
