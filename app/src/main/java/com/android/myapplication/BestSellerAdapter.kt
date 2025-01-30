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
    private var items: List<BookItem>, // 책 목록 데이터
    private val onItemClick: (BookItem) -> Unit // 클릭 이벤트를 처리하는 람다 함수
) : RecyclerView.Adapter<BestSellerAdapter.ViewHolder>() {

    // ViewHolder 클래스: 개별 아이템 뷰를 관리
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookCoverImageView: ImageView = itemView.findViewById(R.id.mf_book_cover) // 책 표지
        val bookTitleTextView: TextView = itemView.findViewById(R.id.mf_book_title) // 책 제목
        val bookAuthorTextView: TextView = itemView.findViewById(R.id.mf_book_author) // 책 저자
    }

    // 새로운 ViewHolder 생성 (RecyclerView가 새로운 아이템을 만들 때 호출됨)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.moreinfo_book_recyclerview, parent, false) // XML을 View 객체로 변환
        return ViewHolder(view)
    }

    // ViewHolder와 데이터를 바인딩하는 메서드 (RecyclerView가 아이템을 화면에 표시할 때 호출됨)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = items[position] // 현재 위치의 책 데이터 가져오기

        // 책 제목과 저자 정보를 TextView에 설정
        holder.bookTitleTextView.text = book.title
        holder.bookAuthorTextView.text = book.author

        // Glide를 사용해 책 표지 이미지를 로드
        Glide.with(holder.itemView.context)
            .load(book.cover)
            .into(holder.bookCoverImageView)

        // 책 클릭 이벤트 설정 (클릭 시 onItemClick 람다 함수 실행)
        holder.itemView.findViewById<LinearLayout>(R.id.moreinfo_bookcover).setOnClickListener {
            onItemClick(book)
        }
    }

    // 전체 아이템 개수 반환
    override fun getItemCount(): Int = items.size

    // 새 데이터를 설정하고 RecyclerView를 갱신하는 메서드
    fun updateBooks(newItems: List<BookItem>) {
        items = newItems
        notifyDataSetChanged() // RecyclerView를 갱신
    }
}
