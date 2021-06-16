package com.zlyandroid.weather.ui.bean

import com.squareup.moshi.Json
import com.zlyandroid.basic.common.mvp.BaseBean

class Now : BaseBean() {
    val obsTime: String = "" //实况观测时间
    val temp: String = "" //实况温度，默认单位：摄氏度
    val feelsLike: String = "" //实况体感温度，默认单位：摄氏度
    val icon: String = "" //当前天气状况和图标的代码，图标可通过天气状况和图标下载
    val text: String = "" //实况天气状况的文字描述，包括阴晴雨雪等天气状态的描述
    val wind360: String = "" // 实况风向360角度
    val windScale: String = "" // 实况风力等级
    val windDir: String = "" // 实况风向
    val windSpeed: String = "" //实况风速，公里/小时
    val humidity: String = "" //实况相对湿度，百分比数值
    val precip: String = "" //实况降水量，默认单位：毫米
    val pressure: String = "" //实况大气压强，默认单位：百帕
    val vis: String = "" //实况能见度，默认单位：公里
    val cloud: String = "" //	实况云量，百分比数值
    val dew: String = ""  //实况露点温度
}