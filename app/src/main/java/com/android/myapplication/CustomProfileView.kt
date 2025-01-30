import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.android.myapplication.R
import com.android.myapplication.databinding.CustomProfileViewBinding

class CustomProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: CustomProfileViewBinding

    init {
        // 뷰 바인딩 초기화
        binding = CustomProfileViewBinding.inflate(LayoutInflater.from(context), this, true)
    }


}
