package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import com.zlyandroid.weather.widget.weather.RainWeather.Companion.RainDrawable
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/12
 * GitHub: https://github.com/ZLYang110
 * desc:雨加雪
 */
class RainSnowWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private var snowDrawable: GradientDrawable? = null
    private var rainDrawable: RainDrawable? = null
    private val snowHolders= ArrayList<SnowWeather.SnowHolder>()
    private val rainHolders = ArrayList<RainWeather.RainHolder>()

    private val SNOW_COUNT = 15
    private val RAIN_COUNT = 30
    private val MIN_SIZE = 6f // dp
    private val MAX_SIZE = 14f // dp


    init {
        snowDrawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(-0x66000001, 0x00ffffff)
        )
        snowDrawable!!.setShape(GradientDrawable.OVAL)
        snowDrawable!!.setGradientType(GradientDrawable.RADIAL_GRADIENT)
        rainDrawable = RainDrawable()
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder in snowHolders) {
            snowDrawable?.let {
                holder.updateRandom(it, alpha)
                snowDrawable!!.draw(canvas)
            }
        }
        for (holder in rainHolders) {
            rainDrawable?.let {
                holder.updateRandom(it, alpha)
                rainDrawable!!.draw(canvas)
            }
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (snowHolders.size == 0) {
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
                    200f
                ) // 40当作中雪
            for (i in 0 until  SNOW_COUNT) {
                val size =
                    getRandom(
                        minSize,
                        maxSize
                    )
                val holder = SnowWeather.SnowHolder(
                    getRandom(
                        0f,
                        width.toFloat()
                    ),
                    size,
                    height.toFloat(),
                    speed
                )
                snowHolders.add(holder)
            }
        }
        if (rainHolders.size == 0) {
            val rainWidth =
                dp2px(2f) //*(1f -  getDownRandFloat(0, 1));
            val minRainHeight =
                dp2px(8f)
            val maxRainHeight =
                dp2px(
                    14f
                )
            val speed =
                dp2px(
                    360f
                )
            for (i in 0 until  RAIN_COUNT) {
                val x =
                    getRandom(
                        0f,
                        width.toFloat()
                    )
                val holder = RainWeather.RainHolder(
                    x, rainWidth, minRainHeight, maxRainHeight,
                    height.toFloat(), speed
                )
                rainHolders.add(holder)
            }
        }

    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) Companion.SkyBackground.RAIN_N else Companion.SkyBackground.RAIN_D
    }

}