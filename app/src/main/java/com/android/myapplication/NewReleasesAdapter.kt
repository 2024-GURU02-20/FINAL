package com.android.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide

class NewReleasesAdapter(
    private var items: List<BookItem> // 표시할 책 데이터 리스트
) : RecyclerView.Adapter<NewReleasesAdapter.ViewHolder>() {

    // ViewHolder: 개별 RecyclerView 아이템을 관리
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.book_cover_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 아이템 레이아웃 파일을 inflate하여 ViewHolder 생성
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book_cover, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position] // 현재 위치의 책 데이터


        Glide.with(holder.itemView.context)
            .load(book.cover) // 책 표지 URL
            .into(holder.bookCoverImageView) // ImageView에 로드
    }

    override fun getItemCount(): Int = items.size // 아이템 개수 반환

    // 데이터를 업데이트하는 함수
    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged() // RecyclerView 업데이트
    }
}
