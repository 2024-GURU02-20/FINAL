package com.android.myapplication

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int, // 열 개수 (Grid Span)
    private val spacing: Int // 간격 크기 (px)
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // 아이템 위치
        if (position >= 0) {
            val column = position % spanCount // 현재 열 위치

            // 가로 간격 설정 (아이템 사이에만 적용)
            outRect.left = if (column == 0) 0 else spacing / 2
            outRect.right = if (column == spanCount - 1) 0 else spacing / 2

            // 세로 간격 설정 (첫 번째 행 제외)
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
