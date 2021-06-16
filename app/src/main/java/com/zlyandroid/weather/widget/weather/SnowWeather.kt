package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/13
 * GitHub: https://github.com/ZLYang110
 * desc:下雪
 */
class SnowWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {


    private var drawable: GradientDrawable? = null
    private val holders = ArrayList<SnowHolder>()

    private val COUNT = 30
    private val MIN_SIZE = 12f // dp

    private val MAX_SIZE = 30f // dp


    init {
        drawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(-0x66000001, 0x00ffffff)
        )
        drawable!!.setShape(GradientDrawable.OVAL)
        drawable!!.setGradientType(GradientDrawable.RADIAL_GRADIENT)
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder in holders) {
            drawable?.let {
                holder.updateRandom(it, alpha)
                drawable!!.draw(canvas)
            }

        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)

        if (holders.size == 0) {
            val minSize =
                dp2px(
                    MIN_SIZE
                )
            val maxSize =
                dp2px(
                    MAX_SIZE
                )
            val speed =
                dp2px(
                    80f
                ) // 40当作中雪80
            for (i in 0 until  COUNT) {
                val size =
                    getRandom(
                        minSize,
                        maxSize
                    )
                val holder =
                    SnowHolder( getRandom(0f, width.toFloat()),
                        size,
                        height.toFloat(),
                        speed
                    )
                holders.add(holder)
            }
        }
    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) Companion.SkyBackground.SNOW_N else Companion.SkyBackground.SNOW_D
    }

    class SnowHolder(
        var x: Float,
        // public float y;//y 表示雨滴底部的y坐标,由curTime求得
        val snowSize: Float,
        // [0,1]
        val maxY: Float,
        averageSpeed: Float
    ) {

        var curTime // [0,1]
                : Float
        val v // 速度
                : Float

        fun updateRandom(drawable: GradientDrawable, alpha: Float) {
            curTime += 0.025f
            val curY = curTime * v
            if (curY - snowSize > maxY) {
                curTime = 0f
            }
            val left = Math.round(x - snowSize / 2f)
            val right = Math.round(x + snowSize / 2f)
            val top = Math.round(curY - snowSize)
            val bottom = Math.round(curY)
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = snowSize / 2.2f
            drawable.alpha = (255 * alpha).toInt()
        }

        /**
         * @param x
         * @param snowSize
         * @param maxY
         * @param averageSpeed
         */
        init {
            v = averageSpeed *  getRandom(0.85f, 1.15f)
            val maxTime = maxY / v
            curTime =  getRandom(0f, maxTime)
        }
    }
}