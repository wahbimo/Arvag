package com.example.arvag.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpaceItemDecoration:ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if(parent.getChildLayoutPosition(view)%2 != 0)
        {
            outRect.top = 0
            outRect.bottom = 0
        }else outRect.top = 0

    }
}