package com.zlyandroid.basic.common.http.rx

import com.zlyandroid.basic.common.http.HttpStatus
import com.zlyandroid.basic.common.http.bean.ResponseBean
import com.zlyandroid.basic.common.http.exception.ExceptionHandle
import com.zlyandroid.basic.common.mvp.BaseView
import com.zlyandroid.basic.common.util.NetWorkUtil
import io.reactivex.observers.ResourceObserver

/**
 * @author zhangliyang
 * @date 2018/6/11
 * @desc
 */
abstract class BaseObserver<T : ResponseBean> : ResourceObserver<T> {

    private var mView: BaseView? = null
    private var mErrorMsg = ""
    private var bShowLoading = true

    constructor(view: BaseView) {
        this.mView = view
    }

    constructor(view: BaseView, bShowLoading: Boolean) {
        this.mView = view
        this.bShowLoading = bShowLoading
    }

    /**
     * 成功的回调
     */
    protected abstract fun onSuccess(t: T)

    /**
     * 错误的回调
     */
    protected fun onError(t: T) {}

    override fun onStart() {
        super.onStart()
        if (bShowLoading) mView?.showLoading()
        if (!NetWorkUtil.isConnected()) {
            mView?.showMsg("当前网络不可用，请检查网络设置")
            onComplete()
        }
    }

    override fun onNext(t: T) {
        mView?.hideLoading()
        when {
            t. errorCode == HttpStatus.SUCCESS -> onSuccess(t)
            t.errorCode == HttpStatus.TOKEN_INVALID -> {
                // TODO Token 过期，重新登录
            }
            else -> {
                onError(t)
                if (t.errorMsg.isNotEmpty())
                    mView?.showMsg(t.errorMsg)
            }
        }
    }

    override fun onError(e: Throwable) {
        mView?.hideLoading()
        if (mView == null) {
            throw RuntimeException("mView can not be null")
        }
        if (mErrorMsg.isEmpty()) {
            mErrorMsg = ExceptionHandle.handleException(e)
        }
        mView?.showMsg(mErrorMsg)
    }

    override fun onComplete() {
        mView?.hideLoading()
    }
}

