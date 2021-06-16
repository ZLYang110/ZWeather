package com.zlyandroid.weather.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.zlyandroid.basic.common.AppCommon
import com.zlyandroid.basic.common.app.App
import com.zlyandroid.basic.common.util.log.L
import java.util.*

/**
 * author: zhangliyang
 * date: 2020/11/12
 */
class ProApplication : App() {


    override fun onCreate() {
        super.onCreate()
        AppConfig.init(this)
        AppCommon.init(this)
    /*    SmartRefreshLayout.setDefaultRefreshHeaderCreator(DefaultRefreshHeaderCreator { context, layout ->
            //layout.setPrimaryColorsId(R.color.colorPrimary, R.color.colorAccent);//全局设置主题颜色
            return@DefaultRefreshHeaderCreator  ClassicsHeader(context)}  )
        SmartRefreshLayout.setDefaultRefreshFooterCreator(DefaultRefreshFooterCreator { context, layout -> return@DefaultRefreshFooterCreator ClassicsFooter(context) })*/
    }

    companion object {



    }



}