package com.zlyandroid.basic.common.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.zlyandroid.basic.common.AppCommon
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 *  desc: 获取资源文件的工具类
 */

object ResUtils {

    fun getResources(): Resources {
        return AppCommon.getApplication().resources
    }

    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(AppCommon.getApplication(), id)
    }

    fun getString(@StringRes id: Int): String? {
        return getResources().getString(id)
    }

    @Deprecated("")
    fun getColor(@ColorRes id: Int): Int {
        return ContextCompat.getColor(AppCommon.getApplication(), id)
    }

    fun getColor(context: Context, @ColorRes id: Int): Int {
        return context.resources.getColor(id)
    }

    fun getColor(view: View, @ColorRes id: Int): Int {
        return view.context.resources.getColor(id)
    }

    fun getDimens(@DimenRes id: Int): Float {
        return getResources().getDimension(id)
    }

    fun getStringArray(@ArrayRes id: Int): Array<String?>? {
        return getResources().getStringArray(id)
    }

    fun getBoolean(@BoolRes id: Int): Boolean {
        return getResources().getBoolean(id)
    }

    fun getInteger(@IntegerRes id: Int): Int {
        return getResources().getInteger(id)
    }

    fun getAssets(fileName: String?): String? {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = AppCommon.getApplication().assets
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(
                        fileName!!
                    )
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}