package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas


/**
 * @author zhangliyang
 * @date 2021/1/13
 * GitHub: https://github.com/ZLYang110
 * desc:默认
 */
class DefaultWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    init {

    }
    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {

        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
    }



}