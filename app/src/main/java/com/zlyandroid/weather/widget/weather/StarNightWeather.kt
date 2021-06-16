package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.Log
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/12
 * GitHub: https://github.com/ZLYang110
 * desc:晴天的晚上 星空
 */
class StarNightWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private var drawable: GradientDrawable? = null
    private val holders = ArrayList<StarHolder>()

    private val STAR_COUNT = 120
    private val STAR_MIN_SIZE = 2f // dp

    private val STAR_MAX_SIZE = 6f // dp


    init {
        drawable = GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(-0x1, 0x00ffffff))
        drawable!!.setShape(GradientDrawable.OVAL)
        drawable!!.setGradientType(GradientDrawable.RADIAL_GRADIENT)
        drawable!!.setGradientRadius((Math.sqrt(2.0) * 60).toFloat())
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder in holders) {
            drawable?.let { holder.updateRandom(it, alpha) }
            // drawable.setBounds(0, 0, 360, 360);
            // drawable.setGradientRadius(360/2.2f);//测试出来2.2比较逼真
            try {
                drawable!!.draw(canvas)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e(
                    "FUCK",
                    "drawable.draw(canvas)->" + drawable!!.bounds.toShortString()
                )
            }
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)

        if (holders.size == 0) {
            val starMinSize: Float =
                dp2px(
                    STAR_MIN_SIZE
                )
            val starMaxSize: Float =
                dp2px(
                    STAR_MAX_SIZE
                )
            for (i in 0 until  STAR_COUNT) {
                val starSize =
                    getRandom(
                        starMinSize,
                        starMaxSize
                    )
                val y =
                    getDownRandFloat(
                        0f,
                        height.toFloat()
                    )
                // 20%的上半部分屏幕最高alpha为1，其余的越靠下最高alpha越小
                val maxAlpha = 0.2f + 0.8f * (1f - y / height)
                val holder =
                    StarHolder( getRandom(0f, width.toFloat()),
                        y,
                        starSize,
                        starSize,
                        maxAlpha
                    )
                holders.add(holder)
            }
            // holders.add(new StarHolder(360, 360, 200, 200));
        }
    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return Companion.SkyBackground.CLEAR_N
    }
    class StarHolder(
        var x: Float,
        var y: Float,
        var w: Float,
        var h: Float,
        // [0,1]
        val maxAlpha: Float
    ) {
        var curAlpha // [0,1]
                : Float
        var alphaIsGrowing = true
        fun updateRandom(drawable: GradientDrawable, alpha: Float) {
            // curAlpha += getRandom(-0.01f, 0.01f);
            // curAlpha = Math.max(0f, Math.min(maxAlpha, curAlpha));
            val delta =
                getRandom(
                    0.003f * maxAlpha,
                    0.012f * maxAlpha
                )
            if (alphaIsGrowing) {
                curAlpha += delta
                if (curAlpha > maxAlpha) {
                    curAlpha = maxAlpha
                    alphaIsGrowing = false
                }
            } else {
                curAlpha -= delta
                if (curAlpha < 0) {
                    curAlpha = 0f
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
                    0f,
                    maxAlpha
                )
        }
    }
}