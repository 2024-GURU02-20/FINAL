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

class BestReviewAdapter(
    private var items: List<BookItem> // 책 리스트
) : RecyclerView.Adapter<BestReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.ivBookCover) // 책 표지
        val bookTitleTextView: TextView = itemView.findViewById(R.id.tvBookTitle) // 책 제목
        val reviewContentTextView: TextView = itemView.findViewById(R.id.tvReviewContent) // 리뷰 내용
        val reviewRatingTextView: TextView = itemView.findViewById(R.id.tvReviewRating) // 별점 (현재 임시로 출판일 사용)
        val reviewLikesTextView: TextView = itemView.findViewById(R.id.tvReviewLikes) // 추천 개수
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booklist_bestreview_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]

        // 책 제목, 리뷰 내용, 별점(출판일), 좋아요 개수 설정
        holder.bookTitleTextView.text = book.title
        holder.reviewContentTextView.text = book.description  // 책 소개를 리뷰 내용처럼 사용
        holder.reviewRatingTextView.text = "★ ${book.pubDate}"  // 출판일을 별점 대신 임시 표시
        holder.reviewLikesTextView.text = "👍 100"  // 좋아요 (임시 값)

        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)

    }

    override fun getItemCount(): Int = items.size

    // 📌 새롭게 추가된 메서드: 데이터를 업데이트하고 RecyclerView 갱신
    fun updateReviews(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
