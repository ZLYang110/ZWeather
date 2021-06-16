package com.zlyandroid.basic.common.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.lang.reflect.Method


/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 *  desc: 弹出软件盘辅助类
 */
object InputMethodUtils {

    /**
     * 禁止EditText弹出软件盘，光标依然正常显示。
     */
    fun disableShowSoftInput(editText: EditText ) {
        val cls = EditText::class.java
        var method: Method
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType)
            method.isAccessible = true
            method.invoke(editText, false)
        } catch (ignore: Exception) {
        }
        try {
            method = cls.getMethod("setSoftInputShownOnFocus", Boolean::class.javaPrimitiveType)
            method.isAccessible = true
            method.invoke(editText, false)
        } catch (ignore: Exception) {
        }
    }

    /**
     * 隐藏虚拟键盘
     */
    fun hide(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(
                v.applicationWindowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 显示虚拟键盘
     */
    fun show(v: View) {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            imm.showSoftInput(v, InputMethodManager.SHOW_FORCED)
        }
    }

    /**
     * 输入法是否显示着
     */
    fun isShow(v: View): Boolean {
        val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return imm.isActive
    }
}