package com.android.myapplication

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kizitonwose.calendar.view.ViewContainer


class MonthHeaderViewContainer(view: View) : ViewContainer(view) {
    val headerText: TextView = view.findViewById(R.id.monthText)
    val titlesContainer = view as ViewGroup 
}
