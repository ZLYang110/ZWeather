package com.zlyandroid.weather.event

import android.content.Context
import com.zlyandroid.weather.ui.dialog.MenuPopup


/**
 * @author zhangliyang
 * @date 2021/6/4
 * GitHub: https://github.com/ZLYang110
 * @dec
 */

class LocationCityEvent(var cityName: String) : BaseEvent() {

    companion object {
        fun postLocationCityEvent(mCityName: String) {
            LocationCityEvent(mCityName).post()
        }
    }

}