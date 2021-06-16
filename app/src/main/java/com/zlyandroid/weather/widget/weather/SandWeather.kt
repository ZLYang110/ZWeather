package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/1/13
 * GitHub: https://github.com/ZLYang110
 * desc:沙
 */
class SandWeather(context: Context, isNight: Boolean) : BaseWeather(context, isNight) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val count = 30
    private val holders =  ArrayList<ArcHolder>()
    init {
        paint.style = Paint.Style.STROKE
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
            val cx = -width * 0.3f
            val cy = -width * 1.5f
            for (i in 0 until count) {
                val radiusWidth =
                    getRandom(
                        width * 1.3f,
                        width * 3.0f
                    )
                val radiusHeight = radiusWidth * getRandom(
                    0.92f,
                    0.96f
                ) //getRandom(width * 0.02f,  width * 1.6f);
                val strokeWidth =
                    dp2px(
                        getDownRandFloat(
                            1f,
                            2.5f
                        )
                    )
                val sizeDegree =
                    getDownRandFloat(
                        8f,
                        15f
                    )
                holders.add(
                    ArcHolder(
                        cx,
                        cy,
                        radiusWidth,
                        radiusHeight,
                        strokeWidth,
                        30f,
                        99f,
                        sizeDegree,
                        if (isNight) -0x665a6faa else -0x445a6faa
                    )
                )
            }
        }
    }

    override fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) Companion.SkyBackground.SAND_N else Companion.SkyBackground.SAND_D
    }
    class ArcHolder(
        private val cx: Float,
        private val cy: Float,
        private val radiusWidth: Float,
        private val radiusHeight: Float,
        private val strokeWidth: Float,
        private val fromDegree: Float,
        private val endDegree: Float,
        private val sizeDegree: Float,
        private val color: Int
    ) {
        private var curDegree: Float
        private val stepDegree: Float
        private val rectF = RectF()
        fun updateAndDraw(
            canvas: Canvas,
            paint: Paint,
            alpha: Float
        ) {
            paint.color =
                convertAlphaColor(
                    alpha * (Color.alpha(color) / 255f),
                    color
                )
            paint.strokeWidth = strokeWidth
            curDegree += stepDegree * getRandom(
                0.8f,
                1.2f
            )
            if (curDegree > endDegree - sizeDegree) {
                curDegree = fromDegree - sizeDegree
            }
            val startAngle = curDegree
            val sweepAngle = sizeDegree
            rectF.left = cx - radiusWidth
            rectF.top = cy - radiusHeight
            rectF.right = cx + radiusWidth
            rectF.bottom = cy + radiusHeight
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
        }

        init {
            curDegree =
                getRandom(
                    fromDegree,
                    endDegree
                )
            stepDegree =
                getRandom(
                    0.4f,
                    0.8f
                )
        }
    }
}