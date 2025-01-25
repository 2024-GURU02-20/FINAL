package com.android.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.model.AladinResponse
import com.android.myapplication.model.BookItem

class SearchResultBookAdapter(
    private val context: Context?,
    private val items: AladinResponse
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
    }

    override fun getItemCount(): Int {
        return bookItems.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.search_result_book_title)
        val authorTextView: TextView = itemView.findViewById(R.id.search_result_book_author)
    }
}
