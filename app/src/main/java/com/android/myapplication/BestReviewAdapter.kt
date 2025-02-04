//package com.android.myapplication
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.android.myapplication.model.BookItem
//import com.bumptech.glide.Glide
//
//class BestReviewAdapter(
//    private var items: List<BookItem> // 책 리스트
//) : RecyclerView.Adapter<BestReviewAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val bookCoverImageView: ImageView = itemView.findViewById(R.id.ivBookCover) // 책 표지
//        val bookTitleTextView: TextView = itemView.findViewById(R.id.tvBookTitle) // 책 제목
//        val reviewContentTextView: TextView = itemView.findViewById(R.id.tvReviewContent) // 리뷰 내용
//        val reviewLikesTextView: TextView = itemView.findViewById(R.id.tvReviewLikes) // 추천 개수
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.booklist_bestreview_recyclerview, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val book = items[position]
//
//        // 책 제목, 리뷰 내용,  좋아요 개수 설정
//        holder.bookTitleTextView.text = book.title
//        holder.reviewContentTextView.text = "리뷰내용"  // 책 소개를 리뷰 내용처럼 사용
//        holder.reviewLikesTextView.text = "좋아요개수"  // 좋아요 (임시 값)
//
//        Glide.with(holder.itemView.context)
//            .load(book.cover)
//            .into(holder.bookCoverImageView)
//
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    // 새롭게 추가된 메서드: 데이터를 업데이트하고 RecyclerView 갱신
//    fun updateReviews(newItems: List<BookItem>) {
//        items = newItems
//        notifyDataSetChanged()
//    }
//}

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
    ////0,1
    //private var items: List<BookItem>
    //////////
    private var items: List<Pair<BookItem, Review>>
    //////////
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
        //val book = items[position]
        val (book, review) = items[position] //  BookItem과 Review 분리

        // 책 제목, 리뷰 내용, 추천 개수 설정
        holder.bookTitleTextView.text = book.title
        /////////
        holder.reviewContentTextView.text = review.review // Review 엔티티의 review 사용
        holder.reviewLikesTextView.text = "${review.likeCount}" // 추천 개수 적용
        holder.ratingBar.rating = review.starRate // 별점 적용
        /////////
        ////0,1
//        holder.reviewContentTextView.text = book.description
//        holder.reviewLikesTextView.text = "추천 ${position * 10 + 10}" // (임시 값: 10, 20, 30)
//        holder.ratingBar.rating = (position % 5 + 1).toFloat() // 별점 1~5 중 랜덤 할당

//        Glide.with(holder.itemView.context)
//            .load(book.cover)
//            .into(holder.bookCoverImageView)
        if (book.cover.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(book.cover)
                .placeholder(R.drawable.brbook) // 기본 이미지 유지
                .error(R.drawable.brbook) // 이미지 로딩 실패 시 기본 이미지
                .into(holder.bookCoverImageView)
        } else {
            holder.bookCoverImageView.setImageResource(R.drawable.brbook) // 기본 이미지 적용
        }
    }

    override fun getItemCount(): Int = items.size

    // 데이터를 업데이트하고 RecyclerView 갱신
    ////0,1
//    fun updateReviews(newItems: List<BookItem>) {
//        items = newItems
//        notifyDataSetChanged()
//    }
    //////////
    fun updateReviews(newItems: List<Pair<BookItem, Review>>) {
        items = newItems
        notifyDataSetChanged()
    }
    ///////////
}
