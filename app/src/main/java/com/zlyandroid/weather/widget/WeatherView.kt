package com.zlyandroid.weather.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.widget.weather.BaseWeather
import com.zlyandroid.weather.widget.weather.WeatherUtil
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * @author zhangliyang
 * @date 2021/1/12
 * GitHub: https://github.com/ZLYang110
 * desc:天气背景自定义
 */

class WeatherView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var preDrawer: BaseWeather
    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var isSwitchWeather=false



    init {
        preDrawer =
            BaseWeather.makeDrawerByType(
                context,
                BaseWeather.Type.DEFAULT
            )
    }

    fun setWeatherType(type: BaseWeather.Type?) {
        if (type == null) {
            return
        }
        isSwitchWeather = true
        preDrawer =BaseWeather.makeDrawerByType(context,type)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h

        preDrawer!!.setSize(mWidth,mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isSwitchWeather){
            isSwitchWeather=false
            preDrawer!!.setSize(mWidth,mHeight)
        }
        preDrawer.draw(canvas,1f)
        invalidate()
    }



    fun setWeatherType(dayType: Int,dayText: String) {
        if (dayText == null) {
            return
        }
        L.i("now="+dayType +"---"+dayText)
        var type :BaseWeather.Type  = BaseWeather.Type.DEFAULT
        var text :String = dayText
        if(dayText.indexOf("晴")>-1){
            text="晴天"
        }
        if(dayText.indexOf("阴")>-1){
            text="阴天"
        }
        if(dayText.indexOf("雨")>-1){
            text="雨天"
        }
        if(dayText.indexOf("雪")>-1){
            text="雪天"
        }
        if(dayText.indexOf("云")>-1){
            text="多云"
        }
        if(dayText.indexOf("雨")>-1 && dayText.indexOf("雪")>-1){
            text="雨加雪"
        }
        if(dayText.indexOf("雾")>-1){
            text="雾"
        }
        if(dayText.indexOf("霾")>-1){
            text="霾"
        }
        if(dayText.indexOf("风")>-1){
            text="风"
        }
        if(dayText.indexOf("沙")>-1){
            text="沙"
        }
        WeatherUtil.getWeatherTypeList().forEach {
            if (it.dec.equals(text) && dayType == it.dayType){
                type =  it.type
            }
        }
        /*

        when (text) {
           "晴" ->{
               if(dayType == 0){
                   type =  BaseWeather.Type.SUNNY
               }else{
                   type =  BaseWeather.Type.STAR_NIGHT
               }
           }
            "阴" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.OVERCAST_DAY
                }else{
                    type = BaseWeather.Type.OVERCAST_NIGHT
                }
            }
            "多云" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.CLOUDY_DAY
                }else{
                    type = BaseWeather.Type.CLOUDY_NIGHT
                }
            }
            "雨" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.RAIN_DAY
                }else{
                    type = BaseWeather.Type.RAIN_NIGHT
                }
            }
            "雪" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.SNOW_DAY
                }else{
                    type = BaseWeather.Type.SNOW_NIGHT
                }
            }
            "雨加雪" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.RAIN_SNOW_DAY
                }else{
                    type = BaseWeather.Type.RAIN_SNOW_NIGHT
                }
            }
            "雾" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.FOG_DAY
                }else{
                    type = BaseWeather.Type.FOG_NIGHT
                }
            }
            "霾" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.HAZE_DAY
                }else{
                    type = BaseWeather.Type.HAZE_NIGHT
                }
            }
            "风" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.WIND_DAY
                }else{
                    type = BaseWeather.Type.WIND_NIGHT
                }
            }
            "沙" ->{
                if(dayType == 0){
                    type = BaseWeather.Type.SAND_DAY
                }else{
                    type = BaseWeather.Type.SAND_NIGHT
                }
            }
        } */
        L.i(type)
        Data.showBg = type
        setWeatherType(type)

    }

}

