package com.zlyandroid.weather.widget.weather

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import com.zlyandroid.weather.widget.weather.*
import java.util.ArrayList

/**
 * @author zhangliyang
 * @date 2021/1/12
 * GitHub: https://github.com/ZLYang110
 * desc:天气属性
 */
 open abstract class BaseWeather(context: Context, isNight: Boolean) {


    enum class Type {
        DEFAULT, SUNNY,STAR_NIGHT,RAIN_DAY,RAIN_NIGHT,SNOW_DAY,SNOW_NIGHT,RAIN_SNOW_DAY,RAIN_SNOW_NIGHT,FOG_DAY,FOG_NIGHT,CLOUDY_DAY,CLOUDY_NIGHT,HAZE_DAY,HAZE_NIGHT
        ,WIND_DAY,WIND_NIGHT,SAND_DAY,SAND_NIGHT,OVERCAST_DAY,OVERCAST_NIGHT
    }

    companion object {

        private var desity = 0f

        object SkyBackground {
            val BLACK = intArrayOf(-0x1000000, -0x1000000)

            val CLEAR_D = intArrayOf(-0xc2663e, -0xb0613b)
            val CLEAR_N = intArrayOf(-0xf4f0db, -0xdad4be)

            // ////////////
            val OVERCAST_D = intArrayOf(-0xccbda1, -0x9e8978) //0xff748798, 0xff617688
            val OVERCAST_N = intArrayOf(-0xd9d6df, -0xdcd6c2) //0xff1b2229, 0xff262921

            // ////////////
            val RAIN_D = intArrayOf(-0xb07f60, -0xb28b72)
            val RAIN_N = intArrayOf(-0xf2f2eb, -0xdddbd1)

            // ////////////
            val FOG_D = intArrayOf(-0x977a69, -0xbbaea5)
            val FOG_N = intArrayOf(-0xd0c3b9, -0xdbcec5)

            // ////////////
            val SNOW_D = intArrayOf(-0xb07f60, -0xb28b72) //临时用RAIN_D凑数的
            val SNOW_N = intArrayOf(-0xe1dfd7, -0xded9d0)

            // ////////////
            val CLOUDY_D = intArrayOf(-0xb07f60, -0xb28b72) //临时用RAIN_D凑数的
            val CLOUDY_N = intArrayOf(-0xf8ead9, -0xdad4be) // 0xff193353 };//{ 0xff0e1623, 0xff222830 }

            // ////////////
            val HAZE_D = intArrayOf(-0x9e9190, -0xb8b9bc) // 0xff999b95, 0xff818e90
            val HAZE_N = intArrayOf(-0xc8c9cc, -0xdadde3)

            // ////////////
            val SAND_D = intArrayOf(-0x4a5f9a, -0x2a3f7a) //0xffa59056
            val SAND_N = intArrayOf(-0xced7e0, -0xaeb7c0)
        }



        open fun makeDrawerByType(context: Context, type: Type): BaseWeather {
            return when (type) {
                Type.SUNNY -> SunnyWeather(context,false)
                Type.STAR_NIGHT -> StarNightWeather(context,true)
                Type.RAIN_DAY -> RainWeather(context, false)//下雨 白天
                Type.RAIN_NIGHT -> RainWeather(context, true)//下雨 晚上
                Type.SNOW_DAY -> SnowWeather(context, false)//雪 白天
                Type.SNOW_NIGHT -> SnowWeather(context, true)//雪 晚上
                Type.RAIN_SNOW_DAY -> RainSnowWeather(context, false)//雨加雪 白天
                Type.RAIN_SNOW_NIGHT -> RainSnowWeather(context, true)//雨加雪 晚上
                Type.FOG_DAY -> FogWeather(context, false)//雾 白天
                Type.FOG_NIGHT -> FogWeather(context, true)//雾 晚上
                Type.CLOUDY_DAY -> CloudyWeather(context, false)//多云 白天
                Type.CLOUDY_NIGHT -> CloudyWeather(context, true)//多云 晚上
                Type.HAZE_DAY -> HazeWeather(context, false)//霾 白天
                Type.HAZE_NIGHT -> HazeWeather(context, true)//霾 晚上
                Type.WIND_DAY -> WindWeather(context, false)//风 白天
                Type.WIND_NIGHT -> WindWeather(context, true)//风 晚上
                Type.SAND_DAY -> SandWeather(context, false)//沙 白天
                Type.SAND_NIGHT -> SandWeather(context, true)//沙 晚上
                Type.OVERCAST_DAY -> OvercastWeather(context, false)//阴天 白天
                Type.OVERCAST_NIGHT -> OvercastWeather(context, true)//阴天 晚上
                else -> DefaultWeather(context, false)
            }
        }



        fun setDesity(desity : Float ){
            Companion.desity =desity
        }


        fun dp2px(dp: Float): Float {
            return dp * desity
        }
        /**
         * [min, max)
         *
         * @param min
         * @param max
         * @return
         */
        public fun getRandom(min: Float, max: Float): Float {
            require(max >= min) {
                "max should bigger than min!!!!"
                // return 0f;
            }
            return (min + Math.random() * (max - min)).toFloat()
        }

        /**
         * 获取[min, max)内的随机数，越大的数概率越小
         * 参考http://blog.csdn.net/loomman/article/details/3861240
         *
         * @param min
         * @param max
         * @return
         */
        fun getDownRandFloat(min: Float, max: Float): Float {
            val bigend = (min + max) * max / 2f
            // Random rd = new Random();
            val x =
                getRandom(
                    min,
                    bigend
                ) // Math.abs(rd.nextInt() % bigend);
            var sum = 0
            var i = 0
            while (i < max) {
                sum += (max - i).toInt()
                if (sum > x) {
                    return i.toFloat()
                }
                i++
            }
            return min
        }

        //	protected float getCurPercent() {
        //		return curPercent;
        //	}
        fun convertAlphaColor(percent: Float, originalColor: Int): Int {
            val newAlpha = (percent * 255).toInt() and 0xFF
            return newAlpha shl 24 or (originalColor and 0xFFFFFF)
        }
    }

     private var skyDrawable: GradientDrawable? = null
     protected var context: Context? = null
     private var desity = 0f
     protected var width: Int = 0
     protected var height: Int = 0
     protected var isNight = false



    init {
        this.context = context
        this.desity = context.resources.displayMetrics.density
        this.isNight = isNight
        setDesity(
            this.desity
        )
    }


    abstract fun drawWeather(canvas: Canvas, alpha: Float): Boolean

    public fun draw(canvas: Canvas, alpha: Float): Boolean {
        drawSkyBackground(canvas, alpha)
        return drawWeather(canvas, alpha)
    }

    protected fun drawSkyBackground(canvas: Canvas, alpha: Float) {
        if (skyDrawable == null) {
            skyDrawable = makeSkyBackground()
            skyDrawable!!.setBounds(0, 0, width, height)
        }
        skyDrawable!!.alpha = Math.round(alpha * 255f)
        skyDrawable!!.draw(canvas)
    }


    fun makeSkyBackground(): GradientDrawable {
        return GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, getSkyBackgroundGradient())
    }


    // needDrawNextFrame
    protected open fun getSkyBackgroundGradient(): IntArray? {
        return if (isNight) SkyBackground.CLEAR_N else SkyBackground.CLEAR_D
    }


    open fun setSize(width: Int, height: Int) {
        if (this.width != width && this.height != height) {
            this.width = width
            this.height = height
            skyDrawable?.setBounds(0, 0, width, height)
        }
    }




}