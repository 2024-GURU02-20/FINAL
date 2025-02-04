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

class MoreInfoAdapter(
    private var items: List<BookItem>,
    private val onItemClick: (BookItem) -> Unit
) : RecyclerView.Adapter<MoreInfoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.mf_book_cover) // 책 표지
        val bookTitleTextView: TextView = itemView.findViewById(R.id.mf_book_title) // 책 제목
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.mf_book_author) // 책 저자
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.moreinfo_book_recyclerview, parent, false) // XML 파일을 View로 변환
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]

        holder.bookTitleTextView.text = book.title
        holder.bookAuthorTextView.text = book.author

        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)

        // 책 클릭
        holder.itemView.findViewById<LinearLayout>(R.id.moreinfo_bookcover).setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
