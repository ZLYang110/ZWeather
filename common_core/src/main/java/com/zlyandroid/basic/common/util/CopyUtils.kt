@file:Suppress("DEPRECATION")

package com.zlyandroid.basic.common.util

import android.content.ClipboardManager
import android.content.Context
import android.widget.TextView
import com.zlyandroid.basic.common.AppCommon


/**
 * @author zhangliyang
 * @date 2020/11/17
 * GitHub: https://github.com/ZLYang110
 * dec： 复制到剪贴板
 */
object CopyUtils {

    fun copyText(text: String) {
        val clipboardManager = AppCommon.getApplication()
            .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.text = text
    }

    fun copyText(textView: TextView) {
        copyText(textView.text.toString())
    }
}