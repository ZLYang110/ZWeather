package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.util.Log
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/13
 * GitHub: https://github.com/ZLYang110
 * desc:霾
 */
class HazeWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private var drawable: GradientDrawable? = null
    private val holders = ArrayList<HazeHolder>()
    private var minDX = 0f
    private var maxDX = 0f
    private var minDY = 0f
    private var maxDY = 0f

    init {
        drawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            if (isNight) intArrayOf(0x55d4ba3f, 0x22d4ba3f) else intArrayOf(-0x77335999, 0x33cca667)
        ) //d4ba3f

        drawable!!.setShape(GradientDrawable.OVAL)
        drawable!!.setGradientType(GradientDrawable.RADIAL_GRADIENT)
//		drawable.setGradientRadius((float)(Math.sqrt(2) * 60));
        //		drawable.setGradientRadius((float)(Math.sqrt(2) * 60));
        minDX = 0.04f
        maxDX = 0.065f //dp2px(1.5f);
        minDY = -0.02f //-dp2px(0.5f);
        maxDY = 0.02f //dp2px(0.5f);

    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder in holders) {
            holder.updateRandom(
                drawable!!,
                minDX,
                maxDX,
                minDY,
                maxDY,
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                alpha
            )
            //				drawable.setBounds(0, 0, 360, 360);
//				drawable.setGradientRadius(360/2.2f);//测试出来2.2比较逼真
            try {
                drawable!!.draw(canvas)
            } catch (e: Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("FUCK", "drawable.draw(canvas)->" + drawable!!.bounds.toShortString())
            }
        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
            val minSize =
                dp2px(
                    0.8f
                )
            val maxSize =
                dp2px(
                    4.4f
                )
            for (i in 0..79) {
                val starSize =
                    getRandom(
                        minSize,
                        maxSize
                    )
                val holder =
                    HazeHolder(
                        getRandom(
                            0f,
                            width.toFloat()
                        ),
                        getDownRandFloat(
                            0f,
                            height.toFloat()
                        ),
                        starSize,
                        starSize
                    )
                holders.add(holder)
            }
//			holders.add(new StarHolder(360, 360, 200, 200));
        }
    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) Companion.SkyBackground.HAZE_N else Companion.SkyBackground.HAZE_D
    }

    class HazeHolder(
        var x: Float,
        var y: Float,
        var w: Float,
        var h: Float
    ) {
        fun updateRandom(
            drawable: GradientDrawable,
            minDX: Float,
            maxDX: Float,
            minDY: Float,
            maxDY: Float,
            minX: Float,
            minY: Float,
            maxX: Float,
            maxY: Float,
            alpha: Float
        ) {
            //alpha 还没用
            require(!(maxDX < minDX || maxDY < minDY)) { "max should bigger than min!!!!" }
            x +=  getRandom(
                minDX,
                maxDX
            ) * w
            y +=  getRandom(
                minDY,
                maxDY
            ) * h
            //			this.x = Math.min(maxX, Math.max(this.x, minX));
//			this.y = Math.min(maxY, Math.max(this.y, minY));
            if (x > maxX) {
                x = minX
            } else if (x < minX) {
                x = maxX
            }
            if (y > maxY) {
                y = minY
            } else if (y < minY) {
                y = maxY
            }
            val left = Math.round(x - w / 2f)
            val right = Math.round(x + w / 2f)
            val top = Math.round(y - h / 2f)
            val bottom = Math.round(y + h / 2f)
            drawable.alpha = (255f * alpha).toInt()
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = w / 2.2f
        }

    }
}