package com.zlyandroid.basic.common.mvp

import com.trello.rxlifecycle4.LifecycleTransformer


/**
 * @author zhangliyang
 * @date 2020/11/12.
 */
interface BaseView {
    /**
     * 显示加载中
     */
    fun showLoading()

    /**
     * 隐藏加载
     */
    fun hideLoading()

    /**
     * 显示信息
     */
    fun showMsg(msg: String)



    /**
     * 绑定生命周期
     */
    fun <T> bindToLife(): LifecycleTransformer<T>?
}
