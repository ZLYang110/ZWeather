package com.zlyandroid.weather.core



/**
 * @author zhangliyang
 * @date 2021/6/4
 * GitHub: https://github.com/ZLYang110
 * @dec sp数据
 */
object AppCon {

    /**
     * 当前显示的城市数据
     */
    object CITY_KEY {
        const val SP_CITY_ID = "show_city_id" //最近一次所选城市ID
        const val SP_CITY_NAME = "show_city_name" //最近一次所选城市名字
        const val SP_CITY_TEMP = "show_city_temp" //最近一次所选城市温度数据
        const val SP_CITY_LOACTION = "show_city_loaction" //最近一次所选城市是否是定位城市
        const val SP_CITY_UPDATATIME = "show_city_updateTime" //最近一次所选城市数据更新时间
    }
}