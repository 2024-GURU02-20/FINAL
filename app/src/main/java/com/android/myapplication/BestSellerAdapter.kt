import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.myapplication.R
import com.android.myapplication.model.BookItem
import com.bumptech.glide.Glide

class BestSellerAdapter(
    private var items: List<BookItem>,
    private val onItemClick: (BookItem) -> Unit // 클릭 이벤트를 처리하기 위한 람다 전달
) : RecyclerView.Adapter<BestSellerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.mf_book_cover)
        val bookTitleTextView: TextView = itemView.findViewById(R.id.mf_book_title)
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.mf_book_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.moreinfo_book_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position]

        // 책 데이터 바인딩
        holder.bookTitleTextView.text = book.title
        holder.bookAuthorTextView.text = book.author
        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)

        // 책표지 클릭 이벤트 처리
        holder.itemView.findViewById<LinearLayout>(R.id.moreinfo_bookcover).setOnClickListener {
            onItemClick(book) // 클릭된 책 데이터를 전달
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}
