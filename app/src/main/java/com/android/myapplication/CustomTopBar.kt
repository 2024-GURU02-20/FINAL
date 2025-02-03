package com.android.myapplication

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.findFragment
import com.android.myapplication.databinding.CustomTopBarBinding
import androidx.navigation.findNavController

class CustomTopBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: CustomTopBarBinding
    var onBackClick: (() -> Unit)? = null

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomTopBarBinding.inflate(inflater, this, true)
        binding.returnBtn.setOnClickListener {
            onBackClick?.invoke() // Fragment에서 처리하도록 콜백 실행
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }
}