package com.zlyandroid.basic.common.http.rx

import com.zlyandroid.basic.common.http.scheduler.IoMainScheduler


/**
 * @author zhangliyang
 * @date 2018/6/11
 * @desc
 */
object SchedulerUtils {

    fun <T> ioToMain(): IoMainScheduler<T> = IoMainScheduler()

}