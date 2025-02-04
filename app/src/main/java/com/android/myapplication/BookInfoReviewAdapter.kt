package com.android.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.databinding.BookInfoReviewRecyclerViewBinding
import com.android.myapplication.model.ReviewItem

class BookInfoReviewAdapter(private var reviews: List<ReviewItem>) :
    RecyclerView.Adapter<BookInfoReviewAdapter.BookInfoReviewHolder>() {

    inner class BookInfoReviewHolder(private val binding: BookInfoReviewRecyclerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewItem) {
            binding.reviewText.text = review.review
            binding.ratingBar.rating = review.starRate
            binding.likeCount.text = review.likeCount.toString()

            // 좋아요 버튼 클릭 이벤트 예제
            binding.likeIcon.setOnClickListener {
                // 좋아요 수 증가 로직 (예제)
                val newLikeCount = review.likeCount + 1
                binding.likeCount.text = newLikeCount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookInfoReviewHolder {
        val binding = BookInfoReviewRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookInfoReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookInfoReviewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    fun updateReviews(newReviews: List<ReviewItem>) {
        this.reviews = newReviews
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = reviews.size
}
