package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/12
 * GitHub: https://github.com/ZLYang110
 * desc:晴天
 */
class SunnyWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private var drawable: GradientDrawable
    private val holders = ArrayList<SunnyHolder>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val SUNNY_COUNT = 4
    private val centerOfWidth = 0.02f

    init {
        //		drawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[] { 0x10ffffff, 0x20ffffff });
        drawable =
            GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(0x20ffffff, 0x10ffffff))
        drawable.setShape(GradientDrawable.OVAL)
        drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT)
//		drawable.setGradientRadius((float) (Math.sqrt(2) * 60));
        //		drawable.setGradientRadius((float) (Math.sqrt(2) * 60));
        paint.color = 0x33ffffff
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        val size: Float = width * centerOfWidth
        for (holder in holders) {
            holder.updateRandom(drawable, alpha)
            drawable.draw(canvas)
        }
        paint.color = Color.argb((alpha * 0.18f * 255f).toInt(), 0xff, 0xff, 0xff)
        canvas.drawCircle(size, size, width * 0.12f, paint)
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)

        if (holders.size == 0) {
            val minSize = width * 0.16f //dp2px(SUNNY_MIN_SIZE);
            val maxSize = width * 1.5f //dp2px(SUNNY_MAX_SIZE);
            val center = width * centerOfWidth
            val deltaSize = (maxSize - minSize) /  SUNNY_COUNT
            for (i in 0 until  SUNNY_COUNT) {
                val curSize =
                    maxSize - i * deltaSize * getRandom(
                        0.9f,
                        1.1f
                    )
                val holder =
                    SunnyHolder(
                        center,
                        center,
                        curSize,
                        curSize
                    )
                holders.add(holder)
            }
        }
    }


    class SunnyHolder(
        var x: Float,
        var y: Float,
        var w: Float,
        var h: Float
    ) {
        val maxAlpha = 1f
        var curAlpha // [0,1]
                : Float
        var alphaIsGrowing = true
        private val minAlpha = 0.5f
        fun updateRandom(drawable: GradientDrawable, alpha: Float) {
            // curAlpha += getRandom(-0.01f, 0.01f);
            // curAlpha = Math.max(0f, Math.min(maxAlpha, curAlpha));
            val delta =
                getRandom(
                    0.002f * maxAlpha,
                    0.005f * maxAlpha
                )
            if (alphaIsGrowing) {
                curAlpha += delta
                if (curAlpha > maxAlpha) {
                    curAlpha = maxAlpha
                    alphaIsGrowing = false
                }
            } else {
                curAlpha -= delta
                if (curAlpha < minAlpha) {
                    curAlpha = minAlpha
                    alphaIsGrowing = true
                }
            }
            val left = Math.round(x - w / 2f)
            val right = Math.round(x + w / 2f)
            val top = Math.round(y - h / 2f)
            val bottom = Math.round(y + h / 2f)
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = w / 2.2f
            drawable.alpha = (255 * curAlpha * alpha).toInt()
        }

        init {
            curAlpha =
                getRandom(
                    minAlpha,
                    maxAlpha
                )
        }
    }
}