package com.zlyandroid.basic.common.base

import android.os.Bundle
import com.trello.rxlifecycle4.LifecycleTransformer
import com.trello.rxlifecycle4.android.ActivityEvent
import com.zlyandroid.basic.common.mvp.BasePresenter
import com.zlyandroid.basic.common.mvp.BaseView
import com.zlyandroid.basic.common.mvp.IModel
import com.zlyandroid.basic.common.mvp.IPresenter

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpActivity< V : BaseView, P : BasePresenter<V>> : BaseActivity(), BaseView {
    protected var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // 初始化Presenter
    override fun initPresenter() {
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    protected abstract fun createPresenter(): P


    override fun showLoading() {
        showLoading("加载中...")
    }

    override  fun hideLoading() {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }
    }
    override fun showMsg(msg: String) {
        showToast(msg)
    }

    override fun onDestroy() {
        mPresenter?.detachView()
        super.onDestroy()
    }

    override fun <T> bindToLife(): LifecycleTransformer<T>? {
        return bindUntilEvent(ActivityEvent.DESTROY)
    }

}