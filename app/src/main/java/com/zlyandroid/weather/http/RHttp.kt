package com.zlyandroid.weather.http

import com.zlyandroid.basic.common.http.RetrofitFactory


/**
 * @author zhangliyang
 * @date 2020/12/01
 * GitHub: https://github.com/ZLYang110
 *  desc:
 */
object RHttp : RetrofitFactory<Api>() {
    override fun baseUrl():String = "https://devapi.qweather.com/"

    override fun getServise(): Class<Api> = Api::class.java

}