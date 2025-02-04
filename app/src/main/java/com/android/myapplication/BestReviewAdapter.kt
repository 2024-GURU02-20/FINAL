package com.android.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.DB.Review
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class BestReviewAdapter(
    private var items: List<Pair<BookItem, Review>>
) : RecyclerView.Adapter<BestReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.ivBookCover) // 책 표지
        val bookTitleTextView: TextView = itemView.findViewById(R.id.tvBookTitle) // 책 제목
        val reviewContentTextView: TextView = itemView.findViewById(R.id.tvReviewContent) // 리뷰 내용
        val reviewLikesTextView: TextView = itemView.findViewById(R.id.tvReviewLikes) // 추천 개수
        val ratingBar: MaterialRatingBar = itemView.findViewById(R.id.ratingBar) // 별점
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booklist_bestreview_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (book, review) = items[position]

        // 책 제목, 리뷰 내용, 추천 개수 설정
        holder.bookTitleTextView.text = book.title
        holder.reviewContentTextView.text = review.review
        holder.reviewLikesTextView.text = "${review.likeCount}"
        holder.ratingBar.rating = review.starRate


        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)
    }

    override fun getItemCount(): Int = items.size


    // 데이터를 업데이트하고 RecyclerView 갱신
    fun updateReviews(newItems: List<Pair<BookItem, Review>>) {
        items = newItems
        notifyDataSetChanged()
    }
}
