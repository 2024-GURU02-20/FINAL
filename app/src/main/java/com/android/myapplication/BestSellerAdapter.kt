package com.android.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide

class BestSellerAdapter(
    private var items: List<BookItem> // 표시할 책 데이터 리스트
) : RecyclerView.Adapter<BestSellerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.mf_book_cover)
        val bookTitleTextView: TextView = itemView.findViewById(R.id.mf_book_title)
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.mf_book_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // bestseller_bookcover.xml 레이아웃을 inflate
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bestseller_bookcover, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]

        // 책 데이터 바인딩
        holder.bookTitleTextView.text = book.title
        holder.bookAuthorTextView.text = book.author
        Glide.with(holder.itemView.context)
            .load(book.cover) // 책 표지 URL
            .into(holder.bookCoverImageView)
    }

    override fun getItemCount(): Int = items.size

    // 데이터를 업데이트하는 함수
    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
