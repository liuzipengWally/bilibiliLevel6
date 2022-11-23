package com.bilibililevel6

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * author：liuzipeng
 * time: 2022/11/22 22:34
 */
class FeedItemDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val verticalMargin =
            parent.context.resources.getDimension(R.dimen.feed_vertical_margin).toInt()
        val horizontalMargin =
            parent.context.resources.getDimension(R.dimen.feed_horizontal_margin).toInt()
        val itemPosition = parent.getChildLayoutPosition(view)
        val spanCount = ((parent.layoutManager) as GridLayoutManager).spanCount

        val column = itemPosition % spanCount
        outRect.left = column * horizontalMargin / spanCount
        outRect.right = horizontalMargin - (column + 1) * horizontalMargin / spanCount

        if (itemPosition >= spanCount) {
            outRect.top = verticalMargin
        }
    }
}