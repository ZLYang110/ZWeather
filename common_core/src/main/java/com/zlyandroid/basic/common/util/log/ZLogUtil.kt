package com.zlyandroid.basic.common.util.log

import android.text.TextUtils
import android.util.Log
/**
 * @author zhangliyang
 * @date 2020/11/13
 * GitHub: https://github.com/ZLYang110
 */
object ZLogUtil {
    private const val TOP_LEFT_CORNER    = '╔'
    private const val BOTTOM_LEFT_CORNER = '╚'
    private const val MIDDLE_CORNER      = '╟'
    private const val DOUBLE_DIVIDER     = "═════════════════════════════════════════════════"
    private const val SINGLE_DIVIDER     = "─────────────────────────────────────────────────"

    val TOP_BORDER               = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val BOTTOM_BORDER            = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER
    val MIDDLE_BORDER            = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER


    fun isEmpty(line: String): Boolean {
        return TextUtils.isEmpty(line) || line == "\n" || line == "\t" || TextUtils.isEmpty(line.trim { it <= ' ' })
    }

    fun printLine(tag: String?, isTop: Boolean) {
        if (isTop) {
            Log.d(tag, TOP_BORDER)
        } else {
            Log.d(tag,  BOTTOM_BORDER)
        }
    }

    fun printmiddle(type: Int, tag: String?) {
        when (type) {
            LogLevel.D -> Log.d(tag,  MIDDLE_BORDER)
            LogLevel.E -> Log.e(tag,  MIDDLE_BORDER)
            LogLevel.W -> Log.w(tag,  MIDDLE_BORDER)
            LogLevel.I -> Log.i(tag,  MIDDLE_BORDER)
        }
    }

    fun printLine(type: Int, tag: String?, isTop: Boolean) {
        if (isTop) {
            when (type) {
                LogLevel.D -> Log.d(tag,  TOP_BORDER)
                LogLevel.E -> Log.e(tag,  TOP_BORDER)
                LogLevel.W -> Log.w(tag,  TOP_BORDER)
                LogLevel.I -> Log.i(tag,  TOP_BORDER)
            }
        } else {
            when (type) {
                LogLevel.D -> Log.d(tag,  BOTTOM_BORDER)
                LogLevel.E -> Log.e(tag,  BOTTOM_BORDER)
                LogLevel.W -> Log.w(tag,  BOTTOM_BORDER)
                LogLevel.I -> Log.i(tag,  BOTTOM_BORDER)
            }
        }
    }
}