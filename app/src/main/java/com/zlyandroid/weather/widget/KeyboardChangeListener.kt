package com.zlyandroid.weather.widget

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener


/**
 * @author zhangliyang
 * @date 2021/1/23
 * GitHub: https://github.com/ZLYang110
 * desc:监听软件盘显示和关闭
 */
object KeyboardChangeListener  {

    private var rootView // activity的根视图
            : View? = null
    var rootViewVisibleHeight // 记录根视图显示的高度
            = 0
    private val onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener? = null

    fun SoftKeyBoardListener(activity: Activity,onSoftKeyBoardChangeListener: OnSoftKeyBoardChangeListener) {
        // 获取activity的根视图
        rootView = activity.window.decorView
        // 监听视图树中全局布局发生改变或者视图树中某个视图的可视状态发生改变
        rootView!!.viewTreeObserver.addOnGlobalLayoutListener(OnGlobalLayoutListener { // 获取当前根视图在屏幕上显示的大小
            val rect = Rect()
            rootView!!.getWindowVisibleDisplayFrame(rect)
            val visibleHeight: Int = rect.height()
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            //根视图显示高度没有变化，可以看做软键盘显示/隐藏状态没有变化
            if (rootViewVisibleHeight == visibleHeight) {
                return@OnGlobalLayoutListener
            }

            // 根视图显示高度变小超过200，可以看做软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                onSoftKeyBoardChangeListener?.keyBoardShow(rootViewVisibleHeight - visibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            // 根视图显示高度变大超过了200，可以看做软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                onSoftKeyBoardChangeListener?.keyBoardHide(visibleHeight - rootViewVisibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }
        })
    }

    interface OnSoftKeyBoardChangeListener {
        fun keyBoardShow(height: Int)
        fun keyBoardHide(height: Int)
    }
}