package com.android.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.DB.Review
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide

class BestReviewAdapter(
    ///// BookItem
    private var items: List<Pair<BookItem, Review>>, // 책 정보 + 리뷰 정보
    private val onRecommendClick: (Int) -> Unit // 추천 버튼 클릭 시 실행할 함수
    /////
) : RecyclerView.Adapter<BestReviewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.ivBookCover)
        val bookTitleTextView: TextView = itemView.findViewById(R.id.tvBookTitle)
        val reviewContentTextView: TextView = itemView.findViewById(R.id.tvReviewContent)
        val reviewRatingTextView: TextView = itemView.findViewById(R.id.tvReviewRating)
        val reviewLikesTextView: TextView = itemView.findViewById(R.id.tvReviewLikes)
        /////
        val recommendButton: LinearLayout = itemView.findViewById(R.id.RecommendBtn)
        /////
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booklist_bestreview_recyclerview, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (book, review) = items[position]

        // 책 정보 및 리뷰 데이터 바인딩
        holder.bookTitleTextView.text = book.title
        holder.reviewContentTextView.text = review.review
        holder.reviewRatingTextView.text = "★ ${review.starRate}"
        holder.reviewLikesTextView.text = review.like.toString()

        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)

        // 추천 버튼 클릭 시 추천 수 증가
        holder.recommendButton.setOnClickListener {
            onRecommendClick(review.reviewId)
        }
    }

    override fun getItemCount(): Int = items.size

//    // 데이터를 업데이트하고 RecyclerView 갱신
//    fun updateReviews(newItems: List<BookItem>) {
    fun updateReviews(newItems: List<Pair<BookItem, Review>>) {
        items = newItems
        notifyDataSetChanged()
    }
}

