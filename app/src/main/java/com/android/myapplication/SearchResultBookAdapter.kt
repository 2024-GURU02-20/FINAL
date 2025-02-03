package com.android.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners

class SearchResultBookAdapter(
    private val context: Context?,
    private val items: AladinResponse,
    private val onItemClick: (BookItem) -> Unit
) : RecyclerView.Adapter<SearchResultBookAdapter.ViewHolder>() {

    private val bookItems: List<BookItem> = items.item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_result_book_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookItems[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        context?.let {
            Glide.with(it)
                .load(book.cover)
                .transform(GranularRoundedCorners(5f, 5f, 5f, 5f))
                .into(holder.coverImageView)

            Glide.with(it)
                .load(book.cover)
                .transform(GranularRoundedCorners(5f, 5f, 5f, 5f))
                .into(holder.backgroundImageView)
        }

        holder.itemView.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.search_result_book).setOnClickListener {
            onItemClick(book)
        }
    }

    override fun getItemCount(): Int {
        return bookItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.search_result_book_cover)
        val titleTextView: TextView = itemView.findViewById(R.id.search_result_book_title)
        val authorTextView: TextView = itemView.findViewById(R.id.search_result_book_author)
        val backgroundImageView: ImageView = itemView.findViewById(R.id.book_background)

    }
}
