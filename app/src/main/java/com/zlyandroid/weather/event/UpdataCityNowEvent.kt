package com.zlyandroid.weather.event


/**
 * @author zhangliyang
 * @date 2021/6/4
 * GitHub: https://github.com/ZLYang110
 * @dec
 */

class UpdataCityNowEvent(var cityName: String, var isLocation: Boolean) : BaseEvent() {

    companion object {
        fun postUpdataCityNowEvent(mCityName: String,isLocation: Boolean) {
            UpdataCityNowEvent(mCityName,isLocation).post()
        }
    }

}