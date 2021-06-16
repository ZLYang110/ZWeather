package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/13
 * GitHub: https://github.com/ZLYang110
 * desc:阴天
 */
class OvercastWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private val paint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    val holders =
        ArrayList<CloudyWeather.CircleHolder>()
    init {

    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder in holders) {
            holder.updateAndDraw(canvas, paint, alpha)
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)

        if (holders.size == 0) {
            holders.add(
                CloudyWeather.CircleHolder(
                    0.20f * width,
                    -0.30f * width,
                    0.06f * width,
                    0.022f * width,
                    0.56f * width,
                    0.0015f,
                    if (isNight) 0x44374d5c else 0x4495a2ab
                )
            )
            holders.add(
                CloudyWeather.CircleHolder(
                    0.59f * width,
                    -0.35f * width,
                    -0.18f * width,
                    0.032f * width,
                    0.6f * width,
                    0.00125f,
                    if (isNight) 0x55374d5c else 0x335a6c78
                )
            )
            holders.add(
                CloudyWeather.CircleHolder(
                    0.9f * width,
                    -0.18f * width,
                    0.08f * width,
                    -0.015f * width,
                    0.42f * width,
                    0.0025f,
                    if (isNight) 0x5a374d5c else 0x556f8a8d
                )
            )
        }
    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) Companion.SkyBackground.OVERCAST_N else Companion.SkyBackground.OVERCAST_D
    }

}