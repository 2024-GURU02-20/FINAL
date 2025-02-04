package com.android.myapplication

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
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
    var onLoginClick: (() -> Unit)? = null  // 콜백 함수 선언

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomProfileViewBinding.inflate(inflater, this, true)

        // loginButton 클릭 이벤트 -> 콜백 함수 실행
        binding.loginBtn.setOnClickListener {
            onLoginClick?.invoke()
        }
    }

    fun setData(title: String, subtitle: String, imageUrl: Uri?, titleTop: Boolean = false) {
        Log.d("로그인 확인", title + subtitle)
        binding.profileTitle.text = title
        binding.profileSubtitle.text = subtitle
        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .circleCrop()
            .into(binding.profileIcon)
        binding.loginBtn.visibility = View.GONE
        binding.profileIcon.visibility = View.VISIBLE
        if (titleTop == true) {
            binding.profileTitleTop.text = title
            binding.profileTitleTop.visibility = View.VISIBLE
            binding.profileTitle.visibility = View.GONE
        }
    }

    // 외부에서 클릭 리스너를 설정할 수 있도록 함수 추가
    fun setOnLoginClickListener(listener: () -> Unit) {
        onLoginClick = listener
    }
}
