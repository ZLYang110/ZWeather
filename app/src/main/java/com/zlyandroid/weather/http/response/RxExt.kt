package com.zlyandroid.weather.http.response

import android.util.Log
import com.zlyandroid.basic.common.http.HttpStatus
import com.zlyandroid.basic.common.http.exception.ExceptionHandle
import com.zlyandroid.basic.common.http.function.RetryWithDelay
import com.zlyandroid.basic.common.http.rx.SchedulerUtils
import com.zlyandroid.basic.common.mvp.BaseModel
import com.zlyandroid.basic.common.mvp.BaseView
import com.zlyandroid.basic.common.util.NetWorkUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author zhangliyang
 * @date 2018/11/20
 * @desc
 */

fun <T : ResponseBean> Observable<T>.go(
    model: BaseModel?,
    view: BaseView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
) {
    this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                view?.hideLoading()
            }

            override fun onSubscribe(d: Disposable) {
                if (isShowLoading) view?.showLoading()
                 model?.addDisposable(d)
                if (!NetWorkUtil.isConnected()) {
                    view?.showMsg("当前网络不可用，请检查网络设置")
                    d.dispose()
                    onComplete()
                }
            }

            override fun onNext(t: T) {
                view?.hideLoading()
                Log.i("OkHttp","---=="+t.code)
                Log.i("OkHttp","---=="+t.updateTime)
               // Log.i("OkHttp","---=="+t.daily)
                when {
                    t.getRCode() == 200 ->
                        onSuccess.invoke(t)
                    t.getRCode()  == HttpStatus.TOKEN_INVALID -> {
                        // Token 过期，重新登录
                    }
                    else -> {
                        if (onError != null) {
                            onError.invoke(t)
                        } else {
                            if (t.getRMsg().isNotEmpty())
                                view?.showMsg(t.getRMsg())
                        }
                    }
                }
            }

            override fun onError(t: Throwable) {
                Log.i("OkHttp","---=="+t.message)
                view?.hideLoading()
                view?.showMsg(ExceptionHandle.handleException(t))
            }
        })
}

fun <T : ResponseBean> Observable<T>.goo(
    view: BaseView?,
    isShowLoading: Boolean = true,
    onSuccess: (T) -> Unit,
    onError: ((T) -> Unit)? = null
): Disposable {
    if (isShowLoading) view?.showLoading()
    return this.compose(SchedulerUtils.ioToMain())
        .retryWhen(RetryWithDelay())
        .subscribe({
            if (isShowLoading) view?.showLoading()
            when {
                it.getRCode()  == HttpStatus.SUCCESS ->
                    onSuccess.invoke(it)
                it.getRCode()  == HttpStatus.TOKEN_INVALID -> {
                    // Token 过期，重新登录
                }
                else -> {
                    if (onError != null) {
                        onError.invoke(it)
                    } else {
                        if (it.getRMsg().isNotEmpty())
                            view?.showMsg(it.getRMsg())
                    }
                }
            }
        }, {
            view?.hideLoading()
            view?.showMsg(ExceptionHandle.handleException(it))
        })
}
