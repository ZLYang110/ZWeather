package com.zlyandroid.basic.common.http.scheduler

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author zhangliyang
 * @date 2018/6/11
 * @desc
 */
class IoMainScheduler<T> : BaseScheduler<T>(Schedulers.io(), AndroidSchedulers.mainThread())
