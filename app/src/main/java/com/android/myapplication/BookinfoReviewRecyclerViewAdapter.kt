package com.android.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.databinding.BookinfoReviewRecyclerviewBinding
import com.android.myapplication.model.ReviewItem

class BookinfoReviewRecyclerViewAdapter(private var reviews: List<ReviewItem>) :
    RecyclerView.Adapter<BookinfoReviewRecyclerViewAdapter.BookinfoReviewRecyclerViewHolder>() {

    inner class BookinfoReviewRecyclerViewHolder(private val binding: BookinfoReviewRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewItem) {
            binding.reviewText.text = review.review
            binding.ratingBar.rating = review.starRate
            binding.likeCount.text = review.like.toString()

            binding.likeIcon.setOnClickListener {
                val newLikeCount = review.like + 1
                binding.likeCount.text = newLikeCount.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookinfoReviewRecyclerViewHolder {
        val binding = BookinfoReviewRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookinfoReviewRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookinfoReviewRecyclerViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    fun updateReviews(newReviews: List<ReviewItem>) {
        this.reviews = newReviews
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = reviews.size
}
