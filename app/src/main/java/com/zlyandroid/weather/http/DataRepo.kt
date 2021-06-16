package com.zlyandroid.weather.http

import com.squareup.moshi.Json
import com.zlyandroid.basic.common.mvp.BaseBean
import com.zlyandroid.weather.http.response.ResponseBean

/**
 * @author zhangliyang
 * @date 2020/12/01
 * GitHub: https://github.com/ZLYang110
 *  desc:
 */
//data class HttpResult<T>(
//    @Json(name = "data") val data: T,
//    @Json(name = "errorCode") val errorCode: Int,
//    @Json(name = "errorMsg") val errorMsg: String
//)

data class HttpResult<T>(
    @Json(name = "now") val now: T, //当前预报
    @Json(name = "daily") val daily: T, //*天预报
    @Json(name = "hourly") val hourly: T,  //逐小时预报
    @Json(name = "location") val location: T  //逐小时预报
) : ResponseBean()


// now
/*data class Now(
    @Json(name = "obsTime") val obsTime: String, //实况观测时间
    @Json(name = "temp") val temp: String, //实况温度，默认单位：摄氏度
    @Json(name = "feelsLike") val feelsLike: String, //实况体感温度，默认单位：摄氏度
    @Json(name = "icon") val icon: String, //当前天气状况和图标的代码，图标可通过天气状况和图标下载
    @Json(name = "text") val text: String, //实况天气状况的文字描述，包括阴晴雨雪等天气状态的描述
    @Json(name = "wind360") val wind360: String, // 实况风向360角度
    @Json(name = "windScale") val windScale: String, // 实况风力等级
    @Json(name = "windDir") val windDir: String, // 实况风向
    @Json(name = "windSpeed") val windSpeed: String, //实况风速，公里/小时
    @Json(name = "humidity") val humidity: String, //实况相对湿度，百分比数值
    @Json(name = "precip") val precip: String, //实况降水量，默认单位：毫米
    @Json(name = "pressure") val pressure: String, //实况大气压强，默认单位：百帕
    @Json(name = "vis") val vis: String, //实况能见度，默认单位：公里
    @Json(name = "cloud") val cloud: String, //	实况云量，百分比数值
    @Json(name = "dew") val dew: String  //实况露点温度
)*/

// now
data class refer(
    @Json(name = "sources") val sources: String,
    @Json(name = "license") val license: String
)

data class Daily(
    @Json(name = "fxDate") val fxDate: String, //预报日期
    @Json(name = "sunrise") val sunrise: String, //日出时间
    @Json(name = "sunset") val sunset: String, //日落时间
    @Json(name = "moonrise") val moonrise: String, //月升时间
    @Json(name = "moonset") val moonset: String, //月落时间
    @Json(name = "moonPhase") val moonPhase: String, //月相名称
    @Json(name = "tempMax") val tempMax: String, //预报当天最高温度
    @Json(name = "tempMin") val tempMin: String, //预报当天最低温度
    @Json(name = "iconDay") val iconDay: String, //预报白天天气状况的图标代码，图标可通过天气状况和图标下载
    @Json(name = "textDay") val textDay: String, //预报白天天气状况文字描述，包括阴晴雨雪等天气状态的描述
    @Json(name = "iconNight") val iconNight: String, //预报夜间天气状况的图标代码，图标可通过天气状况和图标下载
    @Json(name = "textNight") val textNight: String, //预报晚间天气状况文字描述，包括阴晴雨雪等天气状态的描述
    @Json(name = "wind360Day") val wind360Day: String, //预报白天风向360角度
    @Json(name = "windDirDay") val windDirDay: String, //预报白天风向
    @Json(name = "windScaleDay") val windScaleDay: String, //预报白天风力等级
    @Json(name = "windSpeedDay") val windSpeedDay: String, //预报白天风速，公里/小时
    @Json(name = "wind360Night") val wind360Night: String, //预报夜间风向360角度
    @Json(name = "windDirNight") val windDirNight: String, //预报夜间当天风向
    @Json(name = "windScaleNight") val windScaleNight: String, //预报夜间风力等级
    @Json(name = "windSpeedNight") val windSpeedNight: String, //预报夜间风速，公里/小时
    @Json(name = "humidity") val humidity: String, //预报当天相对湿度，百分比数值
    @Json(name = "precip") val precip: String, //预报当天降水量，默认单位：毫米
    @Json(name = "pressure") val pressure: String, //预报当天大气压强，默认单位：百帕
    @Json(name = "vis") val vis: String, //预报当天能见度，默认单位：公里
    @Json(name = "cloud") val cloud: String, //预报当天云量，百分比数值
    @Json(name = "uvIndex") val uvIndex: String  //预报当天紫外线强度指数
)

data class Hourly(
    @Json(name = "fxTime") val fxTime: String, //逐小时预报时间
    @Json(name = "temp") val temp: String, //逐小时预报温度
    @Json(name = "icon") val icon: String, //逐小时预报天气状况图标代码，图标可通过天气状况和图标下载
    @Json(name = "text") val text: String, //逐小时预报天气状况文字描述，包括阴晴雨雪等天气状态的描述
    @Json(name = "wind360") val wind360: String, //逐小时预报风向360角度
    @Json(name = "windDir") val windDir: String, //逐小时预报风向
    @Json(name = "windScale") val windScale: String, //逐小时预报风力等级
    @Json(name = "windSpeed") val windSpeed: String, //逐小时预报风速，公里/小时
    @Json(name = "humidity") val humidity: String, //逐小时预报相对湿度，百分比数值
    @Json(name = "pop") val pop: String, //逐小时预报降水概率，百分比数值，可能为空
    @Json(name = "precip") val precip: String, //逐小时预报降水量，默认单位：毫米
    @Json(name = "pressure") val pressure: String, //逐小时预报大气压强，默认单位：百帕
    @Json(name = "cloud") val cloud: String, //逐小时预报云量，百分比数值
    @Json(name = "dew") val dew: String  //逐小时预报露点温度

)


 data class NowAir  (
    //空气质量
    @Json(name = "pubTime") val pubTime: String, //实时空气质量数据发布时间
    @Json(name = "aqi") val aqi: String, //实时空气质量指数
    @Json(name = "level") val level: String, //实时空气质量指数等级
    @Json(name = "category") val category: String, //实时空气质量指数级别
    @Json(name = "primary") val primary: String, //实时空气质量的主要污染物，空气质量为优时，返回值为NA
    @Json(name = "pm10") val pm10: String, //实时 pm10
    @Json(name = "pm2p5") val pm2p5: String, //实时 pm2.5
    @Json(name = "no2") val no2: String, //实时 二氧化氮
    @Json(name = "so2") val so2: String, //实时 二氧化硫
    @Json(name = "co") val co: String, //实时 一氧化碳
    @Json(name = "o3") val o3: String  //实时 臭氧
)

data class Location  (
    //城市信息查询
    @Json(name = "name") val name: String, //地区/城市名称
    @Json(name = "id") val id: String, //地区/城市ID
    @Json(name = "lat") val lat: String, //地区/城市纬度
    @Json(name = "lon") val lon: String, //地区/城市经度
    @Json(name = "adm2") val adm2: String, //地区/城市的上级行政区划名称
    @Json(name = "adm1") val adm1: String, //地区/城市所属一级行政区域
    @Json(name = "country") val country: String, //地区/城市所属国家名称
    @Json(name = "tz") val tz: String, //地区/城市所在时区
    @Json(name = "utcOffset") val utcOffset: String, //地区/城市目前与UTC时间偏移的小时数，参考详细说明
    @Json(name = "isDst") val isDst: String, //地区/城市是否当前处于夏令时 1 表示当前处于夏令时 0 表示当前不是夏令时
    @Json(name = "type") val type: String,  //地区/城市的属性
    @Json(name = "rank") val rank: String,  // 地区评分
    @Json(name = "fxLink") val fxLink: String  // 该地区的天气预报网页链接，便于嵌入你的网站或应用
)




data class Air(
    val title: String, //实时空气质量
    val aqi: String //实时空气质量指数
)
