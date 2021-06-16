package com.zlyandroid.basic.common.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.zlyandroid.basic.common.mvp.IModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


/**
 * @author zhangliyang
 * @date 2020/11/12.
 */

open class BasePresenter<V : BaseView> : LifecycleObserver {

    protected var mView: V? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    open fun attachView(view: V) {
        this.mView = view
        if (mView is LifecycleOwner) {
            (mView as LifecycleOwner).lifecycle.addObserver(this)

        }
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */
    open fun detachView() {
        this.mView = null
        // 保证activity结束时取消所有正在执行的订阅
        unDispose()

        this.mView = null
        this.mCompositeDisposable = null
    }

    /**
     * View是否绑定
     *
     * @return
     */
    open fun isViewAttached(): Boolean {
        return mView != null

    }

    open fun addDisposable(disposable: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        disposable?.let { mCompositeDisposable?.add(it) }
    }

    private fun unDispose() {
        mCompositeDisposable?.clear()  // 保证Activity结束时取消
        mCompositeDisposable = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        // detachView()
        owner.lifecycle.removeObserver(this)
    }

    open fun checkViewAttached() {
        if (!isViewAttached()) throw MvpViewNotAttachedException()
    }

    private class MvpViewNotAttachedException internal constructor() :
        RuntimeException("Please call IPresenter.attachView(IBaseView) before" + " requesting data to the IPresenter")

}