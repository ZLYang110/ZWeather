package com.zlyandroid.basic.common.base

import android.app.Activity


/**Activity的逻辑接口
 * @author zhangliyang
 * @use implements ActivityPresenter
 * @warn 对象必须是Activity
 */

interface FragmentPresenter : Presenter {

    /**获取Activity
     * @must 在非抽象Activity中 return this;
     */
    fun getActivity(): Activity? //无public导致有时自动生成的getActivity方法会缺少public且对此报错

}