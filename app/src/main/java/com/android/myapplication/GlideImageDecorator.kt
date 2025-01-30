package com.android.myapplication

import android.content.Context
import android.text.TextPaint
import android.text.style.ImageSpan
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.DayViewDecorator

class GlideImageDecorator(
    private val context: Context,
    private val date: CalendarDay,
    private val imageUrl: String
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay./): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        // 이미지를 Glide로 로드해서 날짜에 추가
        val imageView = ImageView(context)
        Glide.with(context)
            .load(imageUrl)
            .into(imageView)

        // ImageSpan을 사용하여 이미지를 날짜에 추가
        view.addSpan(object : Span {
            override fun updateDrawState(paint: TextPaint?) {
                // 이미지를 텍스트 대신 그리기 위해서 처리
            }
        })
    }
    override fun decorate(view: DayViewFacade) {
        val imageSpan = ImageSpan(context, R.drawable.image)
        view.addSpan(imageSpan)  // 이미지를 추가
}
