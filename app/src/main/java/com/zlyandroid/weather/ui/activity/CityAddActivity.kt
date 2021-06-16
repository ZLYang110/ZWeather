package com.zlyandroid.weather.ui.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.google.android.flexbox.FlexboxLayoutManager
import com.zlyandroid.basic.common.base.BaseMvpActivity
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.event.UpdataCityNowEvent
import com.zlyandroid.weather.ui.adapter.CityAdapter
import com.zlyandroid.weather.ui.bean.Now
import com.zlyandroid.weather.ui.presenter.CityListPresenter
import com.zlyandroid.weather.ui.view.CityListView
import com.zlyandroid.weather.util.DataUtils
import kotlinx.android.synthetic.main.activity_citylist.*
import kotlinx.android.synthetic.main.activity_citylist.rv_citylist
import kotlinx.android.synthetic.main.activity_citylist.weatherView
import kotlinx.android.synthetic.main.activity_cityselect.*
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * @author zhangliyang
 * @date 2021/6/5
 * GitHub: https://github.com/ZLYang110
 * desc:城市管理
 */
class CityAddActivity : BaseMvpActivity<CityListView, CityListPresenter>(), CityListView {

     lateinit var mAdapter:CityAdapter

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CityAddActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun createPresenter(): CityListPresenter = CityListPresenter()

    override fun getLayoutID(): Int = R.layout.activity_cityselect

    override fun initView() {
        weatherView.setWeatherType(Data.showBg)
        rv_citylist.setNestedScrollingEnabled(true)
        rv_citylist.setHasFixedSize(true)
        mAdapter = CityAdapter(ArrayList())
        rv_citylist.run {
           layoutManager = FlexboxLayoutManager(context)
            adapter =mAdapter
        }
        mAdapter.setOnItemChildClickListener   { adapter, view, position ->
            mAdapter.closeAll(null)
            val item: CityModel = mAdapter.getItem(position) ?: return@setOnItemChildClickListener
            when (view.getId()) {
                R.id.rl_top ->{
                    if(!TextUtils.isEmpty(Data.locationAddress) && position==0){
                        UpdataCityNowEvent.postUpdataCityNowEvent(item.cityName,true)
                    }else{
                        UpdataCityNowEvent.postUpdataCityNowEvent(item.cityName,false)
                    }
                    finish()
                }
                R.id.iv_delete ->{
                    mAdapter.remove(position)
                    mPresenter?.removeSelectedCity(item.cityName)
                }
            }
        }
        ll_add.setOnClickListener(this)
    }

    override fun initData() {
        mPresenter?.getSelectedCity(true)
    }

    override fun onClick2(v: View) {
        super.onClick2(v)
        when (v.id) {
            R.id.ll_add -> {
                CityListActivity.start(getActivity())
                finish()
            }
        }
    }


    override fun getCityListSuccess(data: List<CityModel>) {
        val dataList = data.toMutableList()
        if(!TextUtils.isEmpty(Data.locationAddress)){
            Data.locationNow?.run {
                var cityModel  =CityModel(Data.locationAddressID,Data.locationAddress,DataUtils<Now>().translateUserInfoTOString(this),false,"",0,System.currentTimeMillis())
                dataList.add(0,cityModel)
            }
        }
        mAdapter.setNewData(dataList)
    }

    override fun getCityListFailed() {

    }

    override fun updataCitySuccess(data: CityModel) {
        mPresenter?.getSelectedCity(false)
    }

    override fun updataCityFailed() {

    }

    override fun removeSuccess() {

    }

    override fun removeFailed() {

    }
}