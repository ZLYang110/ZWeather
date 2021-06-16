package com.zlyandroid.weather.event

import com.zlyandroid.weather.event.BaseEvent

class CloseFragmentEvent(var cityName: String) : BaseEvent() {

    companion object {
        fun postCloseFragmentEvent(mCityName: String) {
            CloseFragmentEvent(mCityName).post()
        }
    }
}