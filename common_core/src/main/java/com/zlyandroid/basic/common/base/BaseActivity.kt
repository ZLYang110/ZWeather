package com.zlyandroid.basic.common.base

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.zlyandroid.basic.common.dialog.LoadingDialog
import com.zlyandroid.basic.common.util.ClickHelper
import com.zlyandroid.basic.common.util.KeyBoardUtil
import com.zlyandroid.basic.common.util.ToastUtils
import com.zlylib.swipeback.SwipeBackDirection
import com.zlylib.swipeback.SwipeBackHelper
import com.zlylib.swipeback.former.ShrinkSwipeBackTransformer
import org.greenrobot.eventbus.EventBus


/**
 * @author zhangliyang
 * @date 2020/11/12.
 * Description：
 */

abstract class BaseActivity() : RxAppCompatActivity(),ActivityPresenter,View.OnClickListener {

    //侧滑
    private lateinit var mSwipeBackHelper: SwipeBackHelper

    private var isAlive = false
    private var isRunning = false

    protected var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBeforeInfo()
        setContentView(getLayoutID())
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this)
        }
        //侧滑返回
        initSwipeBack()

        initPresenter()
        //初始化控件
        initView()
        // 绑定数据
        initData()
    }


    /**
     * 加载页面之前是否做一些操作默认不做操作
     */
    protected open fun initBeforeInfo(){}

    /**
     * 初始化Presenter
     */
    open fun initPresenter() {}

    /**
     * 是否注册事件分发，默认不绑定
     */
    protected open fun isRegisterEventBus(): Boolean{
        return false
    }
    /**
     * 是否注册SwipeBack
     */
    protected fun enableSwipeBack(): Boolean{
        return true
    }

    override fun getActivity(): Activity {
        return this
    }
    fun showLoading(msg: String?) {
        if (!isDestroyed) {
            dialog = LoadingDialog.createLoadingDialog(this, msg)
            dialog!!.show()
        }
    }

    //================================ SwipeBack start======================================
    /**
    * 初始化 SwipeBack
    */
    private fun initSwipeBack() {
        mSwipeBackHelper = SwipeBackHelper.inject(this)
        mSwipeBackHelper.setSwipeBackEnable(swipeBackEnable())
        mSwipeBackHelper.setSwipeBackOnlyEdge(swipeBackOnlyEdge())
        mSwipeBackHelper.setSwipeBackForceEdge(swipeBackForceEdge())
        mSwipeBackHelper.setSwipeBackDirection(swipeBackDirection())
        mSwipeBackHelper.swipeBackLayout.setShadowStartColor(0)
        mSwipeBackHelper.swipeBackLayout.setSwipeBackTransformer(ShrinkSwipeBackTransformer())
    }
    protected open fun swipeBackEnable(): Boolean {
        return true
    }

    protected open fun swipeBackOnlyEdge(): Boolean {
        return false
    }

    protected open fun swipeBackForceEdge(): Boolean {
        return true
    }
    @SwipeBackDirection
    protected open fun swipeBackDirection(): Int {
        return SwipeBackDirection.FROM_LEFT
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mSwipeBackHelper.onPostCreate()
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        mSwipeBackHelper.onEnterAnimationComplete()
    }
    //================================ SwipeBack end ======================================
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

    override fun onClick(v: View) {
        if (!onClick1(v)) {
            ClickHelper.onlyFirstSameView(v, ClickHelper.Callback { onClick2(v) })
        }
    }
    protected open fun showToast(s: String) {
        runOnUiThread { ToastUtils.show(getActivity(), s) }
    }

    protected open fun showToast(s: String, gravity: Int) {
        ToastUtils.show(getActivity(), s, gravity)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun isRunning(): Boolean {
        return isRunning
    }
    override fun isAlive(): Boolean {
        return isAlive
    }

    override fun finish() {
        if (mSwipeBackHelper.finish()) {
            super.finish()
        }
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
        mSwipeBackHelper.onDestroy()
    }


}