package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/12
 * GitHub: https://github.com/ZLYang110
 * desc:下雨
 */
class RainWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    companion object {
        class RainDrawable {
            var x = 0f
            var y = 0f
            var length = 0f
            var paint =
                Paint(Paint.ANTI_ALIAS_FLAG)

            fun setColor(color: Int) {
                paint.color = color
            }

            fun setStrokeWidth(strokeWidth: Float) {
                paint.strokeWidth = strokeWidth
            }

            fun setLocation(x: Float, y: Float, length: Float) {
                this.x = x
                this.y = y
                this.length = length
            }

            fun draw(canvas: Canvas) {
                canvas.drawLine(x, y - length, x, y, paint)
            }

            init {
                paint.style = Paint.Style.STROKE
                //			paint.setStrokeJoin(Paint.Join.ROUND);
//			paint.setStrokeCap(Paint.Cap.ROUND);
            }
        }
    }

    private var drawable: RainDrawable? = null
    private val holders =  ArrayList<RainHolder>()

    private val cfg_count = 50


    init {
        drawable =
            RainDrawable()
    }

    override fun drawWeather(canvas: Canvas, alpha: Float): Boolean {
        for (holder in holders) {
            drawable?.let { holder.updateRandom(it, alpha) }
            drawable!!.draw(canvas)

        }
        return true
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        if (holders.size == 0) {
//			Log.i(TAG, "x->" + x);
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
                    400f
                )
            for (i in 0 until cfg_count) {
                val x =
                    getRandom(
                        0f,
                        width.toFloat()
                    )
                val holder =
                    RainHolder(
                        x, rainWidth, minRainHeight, maxRainHeight,
                        height.toFloat(), speed
                    )
                holders.add(holder)
            }
        }

    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) BaseWeather.Companion.SkyBackground.RAIN_N else BaseWeather.Companion.SkyBackground.RAIN_D
    }
    class RainHolder(
        var x: Float,
        //		public float y;//y 表示雨滴底部的y坐标,由curTime求得
        val rainWidth: Float,
        minRainHeight: Float,
        maxRainHeight: Float,
        maxY: Float,
        speed: Float
    ) {

        val rainHeight: Float
        val maxY // [0,1]
                : Float
        var curTime // [0,1]
                : Float
        val rainColor: Int
        val v //速度
                : Float

        fun updateRandom(drawable: RainDrawable, alpha: Float) {
            curTime += 0.025f
            //			float curY = v0 * curTime + 0.5f * acceleration * curTime * curTime;
            val curY = curTime * v
            if (curY - rainHeight > maxY) {
                curTime = 0f
            }
            drawable.setColor(
                Color.argb(
                    (Color.alpha(rainColor) * alpha).toInt(),
                    0xff,
                    0xff,
                    0xff
                )
            )
            drawable.setStrokeWidth(rainWidth)
            drawable.setLocation(x, curY, rainHeight)
        }
        //		public boolean alphaIsGrowing = true;
        /**
         * @param x 雨滴中心的x坐标
         * @param rainWidth 雨滴宽度
         * @param maxRainHeight  最大的雨滴长度
         * @param maxY 屏幕高度
         */
        init {
            rainHeight =
                getRandom(
                    minRainHeight,
                    maxRainHeight
                )
            rainColor = Color.argb(
                ( getRandom(
                    0.1f,
                    0.5f
                ) * 255f).toInt(),
                0xff,
                0xff,
                0xff
            )
            this.maxY = maxY
            //			this.v0 = 0;//maxY * 0.1f;
            v = speed * getRandom(
                0.9f,
                1.1f
            )
            val maxTime =
                maxY / v //  (float) Math.sqrt(2f * maxY / acceleration);//s = 0.5*a*t^2;
            curTime =
                getRandom(
                    0f,
                    maxTime
                )
        }
    }
}