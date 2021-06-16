package com.zlyandroid.weather.http

import com.zlyandroid.basic.common.http.RetrofitFactory


/**
 * @author zhangliyang
 * @date 2020/12/01
 * GitHub: https://github.com/ZLYang110
 *  desc:
 */
object RHttp2 : RetrofitFactory<Api>() {
    override fun baseUrl():String = "https://geoapi.qweather.com/"

    override fun getServise(): Class<Api> = Api::class.java

}