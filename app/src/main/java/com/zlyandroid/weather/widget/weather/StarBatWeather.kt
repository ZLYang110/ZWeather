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
 * desc:蒲公英
 */
class StarBatWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private var drawable: GradientDrawable? = null
    private val holders = ArrayList<StarHolder>()
    private var minDX = 0f
    private var maxDX = 0f
    private var minDY = 0f
    private var maxDY = 0f


    init {
        drawable =
            GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(-0x1, 0x00ffffff))
        drawable!!.setShape(GradientDrawable.OVAL)
        drawable!!.setGradientType(GradientDrawable.RADIAL_GRADIENT)
        drawable!!.setGradientRadius((Math.sqrt(2.0) * 60).toFloat())
        minDX = 0f
        maxDX =
            dp2px(3f)
        minDY = -dp2px(
            2f
        )
        maxDY =
            dp2px(2f)
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
            //			drawable.setBounds(0, 0, 360, 360);
//			drawable.setGradientRadius(360/2.2f);//测试出来2.2比较逼真
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
            val starMinSize =
                dp2px(
                    0.5f
                )
            val starMaxSize =
                dp2px(
                    44f
                )
            for (i in 0..99) {
                val starSize =
                    getRandom(
                        starMinSize,
                        starMaxSize
                    )
                val holder =
                    StarHolder(
                        getRandom(
                            0f,
                            width.toFloat()
                        ),
                        getRandom(
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
        return Companion.SkyBackground.CLEAR_N
    }
    class StarHolder(
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
            x += getRandom(
                minDX,
                maxDX
            )
            y += getRandom(
                minDY,
                maxDY
            )
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
            drawable.setBounds(left, top, right, bottom)
            drawable.gradientRadius = w / 2.2f
        }

    }
}