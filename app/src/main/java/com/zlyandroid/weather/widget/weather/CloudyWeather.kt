package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/13
 * GitHub: https://github.com/ZLYang110
 * desc:云
 */
class CloudyWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    // final ArrayList<CloudHolder> holders = new ArrayList<CloudHolder>();
    val holders = ArrayList<CircleHolder>()
    private val paint =  Paint(Paint.ANTI_ALIAS_FLAG)

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
                CircleHolder(
                    0.20f * width, -0.30f * width, 0.06f * width, 0.022f * width, 0.56f * width,
                    0.0015f, if (isNight) 0x183c6b8c else 0x28ffffff
                )
            )
            holders.add(
                CircleHolder(
                    0.58f * width, -0.35f * width, -0.15f * width, 0.032f * width, 0.6f * width,
                    0.00125f, if (isNight) 0x223c6b8c else 0x33ffffff
                )
            )
            holders.add(
                CircleHolder(
                    0.9f * width, -0.19f * width, 0.08f * width, -0.015f * width, 0.44f * width,
                    0.0025f, if (isNight) 0x153c6b8c else 0x15ffffff
                )
            )
        }

    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) Companion.SkyBackground.CLEAR_N else Companion.SkyBackground.CLEAR_D
    }
    class CircleHolder(
        private val cx: Float,
        private val cy: Float,
        private val dx: Float,
        private val dy: Float,
        private val radius: Float,
        private val percentSpeed: Float,
        private val color: Int
    ) {
        private var isGrowing = true
        private var curPercent = 0f
        fun updateAndDraw(
            canvas: Canvas,
            paint: Paint,
            alpha: Float
        ) {
            val randomPercentSpeed =
                getRandom(
                    percentSpeed * 0.7f,
                    percentSpeed * 1.3f
                )
            if (isGrowing) {
                curPercent += randomPercentSpeed
                if (curPercent > 1f) {
                    curPercent = 1f
                    isGrowing = false
                }
            } else {
                curPercent -= randomPercentSpeed
                if (curPercent < 0f) {
                    curPercent = 0f
                    isGrowing = true
                }
            }
            val curCX = cx + dx * curPercent
            val curCY = cy + dy * curPercent
            val curColor =
                convertAlphaColor(
                    alpha * (Color.alpha(color) / 255f),
                    color
                )
            paint.color = curColor
            canvas.drawCircle(curCX, curCY, radius, paint)
        }

    }
}