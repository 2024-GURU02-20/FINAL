package com.android.myapplication

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import com.android.myapplication.R
import com.android.myapplication.databinding.CustomProfileViewBinding
import com.bumptech.glide.Glide
import java.net.URI

class CustomProfileView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: CustomProfileViewBinding

    init {
        // XML을 ViewBinding을 통해 연결
        val inflater = LayoutInflater.from(context)
        binding = CustomProfileViewBinding.inflate(inflater, this, true)
    }

    fun setData(title: String, subtitle:String, imageUrl: Uri?) {
        binding.profileTitle.text = title
        binding.profileSubtitle.text = subtitle
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.profile) // 로딩 중 표시할 기본 이미지
            .error(R.drawable.profile) // 에러 발생 시 기본 이미지
            .circleCrop()
            .into(binding.profileIcon)
    }
}
