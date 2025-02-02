package com.android.myapplication

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
        // XML을 ViewBinding을 통해 연결
        val inflater = LayoutInflater.from(context)
        binding = CustomProfileViewBinding.inflate(inflater, this, true)
    }

    // 외부에서 데이터를 설정할 수 있도록 함수 제공
    fun setProfile(name: String, imageRes: Int) {
        binding.profileName.text = name
    }


}
