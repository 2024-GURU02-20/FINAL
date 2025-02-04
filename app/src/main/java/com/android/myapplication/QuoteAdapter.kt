package com.android.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.databinding.BookinfoFavoriteLineRecyclerviewBinding

class QuoteAdapter(private val quotes: List<String>) :
    RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(val binding: BookinfoFavoriteLineRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = BookinfoFavoriteLineRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.binding.quoteText.text = quotes[position]
    }

    override fun getItemCount() = quotes.size
}
