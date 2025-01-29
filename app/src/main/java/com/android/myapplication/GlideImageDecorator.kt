package com.android.myapplication

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewHolder


class GlideImageDecorator(
    private val context: Context,
    private val date: CalendarDay,
    private val imageUrl: String
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay./): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(object : DayViewHolder.Span {
            override fun updateDrawState(ds: DayViewHolder) {
                val imageView = ImageView(context)
                Glide.with(context).load(imageUrl).into(imageView)
                ds.customView = imageView
            }
        })
    }
}
