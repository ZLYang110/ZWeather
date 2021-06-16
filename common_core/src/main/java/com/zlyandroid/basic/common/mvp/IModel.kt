package com.zlyandroid.basic.common.mvp

import io.reactivex.disposables.Disposable

/**
 * @author zhangliyang
 * @date 2018/4/24.
 * Description：
 */
interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}