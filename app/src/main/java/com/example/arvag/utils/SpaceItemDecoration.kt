package com.example.arvag.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * Item decoration for adding spacing between items in a RecyclerView.
 */
class SpaceItemDecoration:ItemDecoration() {

    /**
     * Adds spacing between items in a RecyclerView.
     *
     * @param outRect The {@link Rect} object to receive the output rectangle.
     * @param view    The {@link View} representing the item view.
     * @param parent  The {@link RecyclerView} containing the item views.
     * @param state   The current state of RecyclerView.
     */
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