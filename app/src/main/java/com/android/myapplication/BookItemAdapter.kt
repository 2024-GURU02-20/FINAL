package com.android.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.model.BookItem
//import com.bumptech.glide.Glide

class BookItemAdapter(
    private val context: Context?,
    private val items: List<BookItem>
) : RecyclerView.Adapter<BookItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.book_cover_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book_cover, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]

        //// Glide가 문제인걸???? ////
        // Glide를 사용해 책 표지 이미지 로드
//        Glide.with(context!!)
//            .load(book.cover) // cover는 URL
//            .into(holder.bookCoverImageView)
    }

    override fun getItemCount(): Int = items.size
}
