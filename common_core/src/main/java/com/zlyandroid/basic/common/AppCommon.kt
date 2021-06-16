package com.zlyandroid.basic.common

import android.app.Application
import android.content.Context
import com.zlyandroid.basic.common.util.log.L
import com.zlylib.upperdialog.utils.Utils


/**
 * @author zhangliyang
 * @date 2020/11/25
 * GitHub: https://github.com/ZLYang110
 * desc 用来初始化项目所需要的配置
 */
object AppCommon {

    const val TAG = "AppConfig"

    var debug = true
    private var application: Application? = null


    fun init(application: Application) {
        AppCommon.application = application
    }


       fun  getApplication(): Application {
        if (application == null) {
            throw RuntimeException("Utils未在Application中初始化")
        }
        return application!!
    }
    fun openDebug() {
        debug = true
    }
}