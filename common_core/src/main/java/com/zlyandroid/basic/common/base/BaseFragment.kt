package com.zlyandroid.basic.common.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.trello.rxlifecycle4.components.support.RxFragment
import com.zlyandroid.basic.common.base.FragmentPresenter
import com.zlyandroid.basic.common.dialog.LoadingDialog
import com.zlyandroid.basic.common.util.ClickHelper
import com.zlyandroid.basic.common.util.ToastUtils
import org.greenrobot.eventbus.EventBus


/**
 * @author zhangliyang
 * @date 2020/11/12.
 * Description：
 */

abstract class BaseFragment() : RxFragment(), FragmentPresenter, View.OnClickListener {

    protected var mRootView: View? = null

    private var isAlive = false
    private var isRunning = false

    protected var dialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBeforeInfo()
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(), container, false)
        }
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
        isAlive = true

        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPresenter();
        //初始化控件
        initView()
        // 绑定数据
        initData()
    }


    override fun getContext(): FragmentActivity {
        return super.requireActivity()
    }

    /**
     * 加载页面之前是否做一些操作默认不做操作
     */
    open fun initBeforeInfo() {}

    /**
     * 初始化Presenter
     */
    open fun initPresenter() {}

    /**
     * 是否注册事件分发，默认不绑定
     */
    protected fun isRegisterEventBus(): Boolean {
        return false;
    }

    /**
     * 点击事件，可连续点击
     */
    protected open fun onClick1(v: View): Boolean {
        Log.v("isFastDoubleClick", "短时间内按钮多次触发1111")
        return false
    }

    /**
     * 点击事件，500毫秒第一次
     */
    protected open fun onClick2(v: View) {
        Log.v("isFastDoubleClick", "短时间内按钮多次触发2222")
    }

    override fun onClick(view: View) {
        if (!onClick1(view)) {
            ClickHelper.onlyFirstSameView(view, ClickHelper.Callback { onClick2(view) })
        }
    }

    fun showLoading(msg: String?) {
        if (isAlive) {
            dialog = LoadingDialog.createLoadingDialog(context, msg)
            dialog!!.show()
        }
    }

    protected open fun showToast(s: String) {
        ToastUtils.show(getActivity(), s)
    }


    override fun isRunning(): Boolean {
        return isRunning
    }

    override fun isAlive(): Boolean {
        return isAlive
    }


    override fun onResume() {
        super.onResume()
        isRunning = true
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        isAlive = false
        isRunning = false
    }

}