package com.zlyandroid.weather.ui.activity

import android.Manifest
import android.view.KeyEvent
import android.view.animation.AnimationUtils
import com.zlyandroid.basic.common.base.BaseMvpActivity
import com.zlyandroid.basic.common.util.PermissionUtils
import com.zlyandroid.basic.common.util.PreUtils
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.AppCon
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.ui.presenter.CityListPresenter
import com.zlyandroid.weather.ui.view.CityListView
import com.zlylib.mypermissionlib.RequestListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_launch.*
import java.util.concurrent.TimeUnit


/**
 * @author zhangliyang
 * @date 2021/1/09
 * GitHub: https://github.com/ZLYang110
 * desc:初始页
 */
class LaunchActivity: BaseMvpActivity<CityListView, CityListPresenter>(), CityListView {

    override fun createPresenter(): CityListPresenter = CityListPresenter()

    override fun getLayoutID(): Int  = R.layout.activity_launch

    override fun initView() {

        Observable.timer(2, TimeUnit.SECONDS )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { aLong: Long? -> enterHome() }

        val top_in =  AnimationUtils.loadAnimation(getActivity(), R.anim.top_in)
        app_icon.startAnimation(top_in)
    }

    override fun initData() {
        mPresenter?.getList(getActivity())
        mPresenter?.getDBNow(PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_NAME,"北京"))
    }
    //定位
    private fun doPermission() {
        PermissionUtils.request(object : RequestListener {
            override fun onSuccess() {
                WeatherActivity.start(getActivity())

            }
            override fun onFailed() {
            }

        }, getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun enterHome() {
        doPermission()

    }

    override fun getCityListSuccess(data: List<CityModel>) {

    }

    override fun getCityListFailed() {

    }

    override fun updataCitySuccess(data: CityModel) {

    }

    override fun updataCityFailed() {

    }

    override fun removeSuccess() {

    }

    override fun removeFailed() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.onKeyDown(keyCode, event)
    }
}