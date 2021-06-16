package com.zlyandroid.weather.widget.weather

import com.zlyandroid.weather.R
import java.util.ArrayList

object WeatherUtil {

    private var typeList: MutableList<WeatherDec>  = ArrayList()
    open fun getWeatherTypeList(): MutableList<WeatherDec> {
       if(typeList.size==0){
           typeList.add(WeatherDec( BaseWeather.Type.SUNNY,"晴天",BaseWeather.Companion.SkyBackground.CLEAR_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.STAR_NIGHT,"晴天",BaseWeather.Companion.SkyBackground.CLEAR_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.OVERCAST_DAY,"阴天",BaseWeather.Companion.SkyBackground.OVERCAST_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.OVERCAST_NIGHT,"阴天",BaseWeather.Companion.SkyBackground.OVERCAST_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.CLOUDY_DAY,"多云",BaseWeather.Companion.SkyBackground.CLEAR_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.CLOUDY_NIGHT,"多云",BaseWeather.Companion.SkyBackground.CLEAR_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.RAIN_DAY,"雨天",BaseWeather.Companion.SkyBackground.RAIN_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.RAIN_NIGHT,"雨天",BaseWeather.Companion.SkyBackground.RAIN_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.SNOW_DAY,"雪天",BaseWeather.Companion.SkyBackground.SNOW_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.SNOW_NIGHT,"雪天",BaseWeather.Companion.SkyBackground.SNOW_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.RAIN_SNOW_DAY,"雨加雪",BaseWeather.Companion.SkyBackground.RAIN_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.RAIN_SNOW_NIGHT,"雨加雪",BaseWeather.Companion.SkyBackground.RAIN_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.FOG_DAY,"雾",BaseWeather.Companion.SkyBackground.FOG_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.FOG_NIGHT,"雾",BaseWeather.Companion.SkyBackground.FOG_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.HAZE_DAY,"霾",BaseWeather.Companion.SkyBackground.HAZE_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.HAZE_NIGHT,"霾",BaseWeather.Companion.SkyBackground.HAZE_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.WIND_DAY,"风",BaseWeather.Companion.SkyBackground.RAIN_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.WIND_NIGHT,"风",BaseWeather.Companion.SkyBackground.RAIN_N,1))
           typeList.add(WeatherDec( BaseWeather.Type.SAND_DAY,"沙",BaseWeather.Companion.SkyBackground.SAND_D,0))
           typeList.add(WeatherDec( BaseWeather.Type.SAND_NIGHT,"沙",BaseWeather.Companion.SkyBackground.SAND_N,1))
       }
        return typeList
    }

    open fun getWeatherTypeBg(dayType: Int,dayText:String): Int {
        var text :String = dayText
        var bg :Int = 0

        if(dayText.indexOf("雨")>-1){
            text="雨"
        }
        if(dayText.indexOf("雪")>-1){
            text="雪"
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
        when (text) {
            "晴" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_fine_d
                }else{
                    bg =  R.drawable.city_item_fine_n
                }
            }
            "阴" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_overcast_d
                }else{
                    bg =  R.drawable.city_item_overcast_n
                }
            }
            "多云" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_cloudy_d
                }else{
                    bg =  R.drawable.city_item_cloudy_n
                }
            }
            "雨" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_rain_d
                }else{
                    bg =  R.drawable.city_item_rain_n
                }
            }
            "雪" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_snow_d
                }else{
                    bg =  R.drawable.city_item_snow_n
                }
            }
            "雨加雪" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_snow_d
                }else{
                    bg =  R.drawable.city_item_snow_n
                }
            }
            "雾" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_fog_d
                }else{
                    bg =  R.drawable.city_item_fog_n
                }
            }
            "霾" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_haze_d
                }else{
                    bg =  R.drawable.city_item_haze_n
                }
            }
            "风" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_wind_d
                }else{
                    bg =  R.drawable.city_item_wind_n
                }
            }
            "沙" ->{
                if(dayType == 0){
                    bg =  R.drawable.city_item_sand_d
                }else{
                    bg =  R.drawable.city_item_sand_n
                }
            }
        }
        return bg
    }

}


data class WeatherDec(
    val type: BaseWeather.Type, //天气类型
    val dec: String, //天气描述
    val color: IntArray, //颜色值
    val dayType: Int //0 白天 1晚上
)