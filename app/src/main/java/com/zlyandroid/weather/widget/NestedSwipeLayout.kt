package com.zlyandroid.weather.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ScrollingView
import com.daimajia.swipe.SwipeLayout

class NestedSwipeLayout : SwipeLayout ,ScrollingView {

    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr)

    override fun canScrollVertically(direction: Int): Boolean {
        val state = openStatus
        val edge = dragEdge
        when (edge) {
            DragEdge.Top -> if (direction < 0) {
                return state != Status.Close
            } else if (direction > 0) {
                return state != Status.Open
            }
            DragEdge.Bottom -> if (direction < 0) {
                return state != Status.Open
            } else if (direction > 0) {
                return state != Status.Close
            }
        }
        return false
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        val state = openStatus
        val edge = dragEdge
        when (edge) {
            DragEdge.Left -> if (direction < 0) {
                return state != Status.Close
            } else if (direction > 0) {
                return state != Status.Open
            }
            DragEdge.Right -> if (direction < 0) {
                return state != Status.Open
            } else if (direction > 0) {
                return state != Status.Close
            }
        }
        return false
    }

    override fun computeHorizontalScrollRange(): Int {
        return dragDistance + computeHorizontalScrollExtent()
    }

    override fun computeHorizontalScrollOffset(): Int {
        val state = openStatus
        val edge = dragEdge
        when (edge) {
            DragEdge.Left -> return if (state == Status.Close) {
                0
            } else {
                -dragDistance
            }
            DragEdge.Right -> return if (state == Status.Close) {
                0
            } else {
                dragDistance
            }
        }
        return 0
    }

    override fun computeHorizontalScrollExtent(): Int {
        return width
    }

    override fun computeVerticalScrollRange(): Int {
        return dragDistance + computeVerticalScrollExtent()
    }

    override fun computeVerticalScrollOffset(): Int {
        val state = openStatus
        val edge = dragEdge
        when (edge) {
            DragEdge.Top -> return if (state == Status.Close) {
                0
            } else {
                -dragDistance
            }
            DragEdge.Bottom -> return if (state == Status.Close) {
                0
            } else {
                dragDistance
            }
        }
        return 0
    }

    override fun computeVerticalScrollExtent(): Int {
        return height
    }
}