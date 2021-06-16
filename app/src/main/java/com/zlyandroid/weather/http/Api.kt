package com.zlyandroid.weather.http

import com.zlyandroid.weather.http.response.ResponseBean
import com.zlyandroid.weather.ui.bean.Now
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author zhangliyang
 * @date 2020/12/01
 * GitHub: https://github.com/ZLYang110
 *  desc:
 */
interface Api {

    /**
     * 现在的天气
     * @param location  需要查询地区的LocationID或以英文逗号分隔的经度,纬度坐标（十进制），LocationID可通过城市搜索服务获取。例如 location=101010100 或 location=116.41,39.92
     */

    @GET("v7/weather/now")
    fun nowWeather(@Query("location") location: String, @Query("key") key: String)
            : Observable<HttpResult<Now>>

    /**
     * 24h天气
     */

    @GET("v7/weather/24h")
    fun query24h(@Query("location") location: String, @Query("key") key: String)
            : Observable<HttpResult<MutableList<Hourly>>>
    /**
     * 7天天气
     */

    @GET("v7/weather/7d")
    fun query7d(@Query("location") location: String, @Query("key") key: String)
            : Observable<HttpResult<MutableList<Daily>>>

    /**
     * 空气质量
     */

    @GET("v7/air/now")
    fun nowAir(@Query("location") location: String, @Query("key") key: String)
            : Observable<HttpResult<NowAir>>

    /**
     * 城市信息查询
     * @param location  需要查询地区的名称，支持文字、以英文逗号分隔的经度,纬度坐标（十进制）、LocationID或Adcode（仅限中国城市）。例如 location=北京 或 location=116.41,39.92
     *
     */

    @GET("v2/city/lookup")
    fun lookup(@Query("location") location: String, @Query("key") key: String)
            : Observable<HttpResult<MutableList<Location>>>
}