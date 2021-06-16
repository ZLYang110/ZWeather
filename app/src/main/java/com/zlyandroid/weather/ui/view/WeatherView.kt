package com.zlyandroid.weather.ui.view

import com.zlyandroid.basic.common.mvp.BaseView
import com.zlyandroid.weather.http.*
import com.zlyandroid.weather.ui.bean.Now


/**
 * @author zhangliyang
 * @date 2020/11/14
 * GitHub: https://github.com/ZLYang110
 */
interface WeatherView : BaseView {
    fun nowSuccess(data: Now, updataTime:String)
    fun nowFailed()

    fun query24hSuccess(data: MutableList<Hourly>)
    fun query24hFailed()

    fun query7dSuccess(data: MutableList<Daily>)
    fun query7dFailed()

    fun nowAirSuccess(data: NowAir)
    fun nowAirFailed()


}