package com.zlyandroid.weather.event

import org.greenrobot.eventbus.EventBus

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */

open class BaseEvent {

    fun post() {
        EventBus.getDefault().post(this)
    }

    fun postSticky() {
        EventBus.getDefault().postSticky(this)
    }

    fun removeSticky() {
        EventBus.getDefault().removeStickyEvent(this)
    }
}