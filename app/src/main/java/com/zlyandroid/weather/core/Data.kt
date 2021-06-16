package com.zlyandroid.weather.core

import com.zlyandroid.basic.common.listener.SimpleCallback
import com.zlyandroid.basic.common.listener.SimpleListener
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.db.executor.CityListExecutor
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.ui.bean.Now
import com.zlyandroid.weather.widget.weather.BaseWeather


object Data {

    /**
     * 现在是白天还是晚上
     * */
    var dayType = -1  // 0 白天 1 夜晚

    /**
    * 当前显示的城市的背景
    * */
    var showBg : BaseWeather.Type= BaseWeather.Type.SUNNY //当前使用的背景



    /**
     * 定位的区域
     * */
    var locationAddress: String =""
    /**
     * 定位的区域的ID
     * */
    var locationAddressID: String =""
    /**
     * 定位区域的天气状况
     * */
    var locationNow: Now? =null


    /**
     * 初始话城市和天气
     * */
    var showNow: Now? =null
    var showUpdateTime:String =""
}