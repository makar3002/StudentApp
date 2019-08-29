package com.unitbean.studentappkotlin.ui.lessonsSchedule.itemDecorations

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView

class OffsetItemDecoration(val context: Context?) : RecyclerView.ItemDecoration() {

    val STANDART_PADDING = 30

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State ) {
        super.getItemOffsets(outRect, view, parent, state)
        val offset: Int = (getScreenWidth(parent) / (2f)).toInt() - view.layoutParams.width / 2
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        if (parent.getChildAdapterPosition(view) == 0) {
            setupOutRect(outRect, offset, STANDART_PADDING)
        } else if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            setupOutRect(outRect, STANDART_PADDING, offset)
        }
        else setupOutRect(outRect, STANDART_PADDING, STANDART_PADDING)
    }

    private fun setupOutRect(rect: Rect, offsetLeft: Int, offsetRight: Int) {
        rect.left = offsetLeft
        rect.right = offsetRight
    }

    private fun getScreenWidth(parent: RecyclerView): Int {

//        val windowsManager: WindowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val display: Display = windowsManager.defaultDisplay
//        val size = Point()
//        display.getSize(size)
        return parent.width - parent.marginStart - parent.marginEnd
    }
}