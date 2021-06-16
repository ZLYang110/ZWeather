package com.zlyandroid.weather.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.zlyandroid.basic.common.base.BaseMvpActivity
import com.zlyandroid.basic.common.config.ARouterConfig
import com.zlyandroid.basic.common.util.PreUtils
import com.zlyandroid.basic.common.util.SmartRefreshUtils
import com.zlyandroid.basic.common.util.StatusBarUtil
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.AppCon
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.core.amap.AMapLocationEngine
import com.zlyandroid.weather.event.LocationCityEvent
import com.zlyandroid.weather.event.UpdataCityNowEvent
import com.zlyandroid.weather.http.Air
import com.zlyandroid.weather.http.Daily
import com.zlyandroid.weather.http.Hourly
import com.zlyandroid.weather.http.NowAir
import com.zlyandroid.weather.ui.adapter.AirAdapter
import com.zlyandroid.weather.ui.adapter.D24hAdapter
import com.zlyandroid.weather.ui.adapter.DayAdapter
import com.zlyandroid.weather.ui.bean.Now
import com.zlyandroid.weather.ui.dialog.MenuPopup
import com.zlyandroid.weather.ui.dialog.SimulateDialog
import com.zlyandroid.weather.ui.presenter.WeatherPresenter
import com.zlyandroid.weather.ui.view.WeatherView
import com.zlyandroid.weather.util.DateUtil
import com.zlyandroid.weather.widget.weather.WeatherDec
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.view_weather_content.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.os.Looper

import android.app.IntentService
import java.lang.Exception


/**
 * @author zhangliyang
 * @date 2021/1/09
 * GitHub: https://github.com/ZLYang110
 * desc:天气
 */

class WeatherActivity : BaseMvpActivity<WeatherView, WeatherPresenter>(), WeatherView,
    AppBarLayout.OnOffsetChangedListener {

    private lateinit var mSmartRefreshUtils: SmartRefreshUtils
    private var mD24hdatas: MutableList<Hourly> = ArrayList()


    var dayText = ""  //天气状况的文字描述，包括阴晴雨雪等天气状态的描述

    private val mD24hAdapter: D24hAdapter by lazy {
        D24hAdapter(ArrayList())
    }
    private val mDayAdapter: DayAdapter by lazy {
        DayAdapter(ArrayList())
    }   
    private val mAirAdapter: AirAdapter by lazy {
        AirAdapter(ArrayList())
    }
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WeatherActivity::class.java)
            context.startActivity(intent)
        }
    }

    //定位结束
    @Subscribe(threadMode = ThreadMode.MAIN )
    fun onLocationCityEvent(event: LocationCityEvent) {
        L.i("定位 ==="+event.cityName)
        if(!TextUtils.isEmpty(event.cityName)){
            PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_LOACTION,true)
            mPresenter?.lookup(event.cityName,false)
        }else{
            PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_LOACTION,false)
            ctl_main.setTitle(PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_NAME,"北京"))
            mPresenter?.getDBNow(PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_NAME,"北京"))
        }
    }
    //定位结束
    @Subscribe(threadMode = ThreadMode.MAIN )
    fun onUpdataCityNowEvent(event: UpdataCityNowEvent) {
        L.i("更新城市 ==="+event.cityName)
        showLoading()
        if(event.isLocation){
            AMapLocationEngine.init()
        }else{
            PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_LOACTION,false)
            ctl_main.setTitle(event.cityName)
            mPresenter?.getDBNow(event.cityName)
        }
    }



    override fun isRegisterEventBus(): Boolean = true
    override fun swipeBackEnable(): Boolean = false
    override fun createPresenter(): WeatherPresenter = WeatherPresenter()
    override fun initBeforeInfo() {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(getActivity())
    }

    override fun getLayoutID(): Int = R.layout.activity_weather
    override fun initView() {
        AMapLocationEngine.init()
        //StatusBarUtil.setMargin(getActivity(), header)
        StatusBarUtil.setPaddingSmart(getActivity(), toolbar)
        StatusBarUtil.setPaddingSmart(getActivity(), toolbar1)
        ctl_main.setCollapsedTitleTextColor(Color.WHITE)
        ctl_main.setExpandedTitleColor(Color.WHITE)
        /*  mSmartRefreshUtils = SmartRefreshUtils.with(srl)
          mSmartRefreshUtils.pureScrollMode()
          mSmartRefreshUtils.setRefreshListener(object : SmartRefreshUtils.RefreshListener {
              override fun onRefresh() {
                  mPresenter?.now("101010100")
              }
          })*/
        abl_main.addOnOffsetChangedListener(this)
        tv_more.setOnClickListener(this)
    }



    override fun initData() {
        ll_view_24h.visibility= View.INVISIBLE
        ll_view_7d.visibility= View.INVISIBLE
        ll_view_air.visibility= View.INVISIBLE
        ll_view_sun.visibility= View.INVISIBLE
        rv_24h.run {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = mD24hAdapter
        }
        rv_7d.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mDayAdapter
        }
        rv_air.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAirAdapter
        }

        Data.showNow?.let {
            ctl_main.setTitle(PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_NAME,"北京"))
            nowSuccess(it, Data.showUpdateTime) }
    }

    override fun onClick2(v: View) {
        super.onClick2(v)
        when (v.id) {
            R.id.tv_more -> {
                MenuPopup.with(getActivity()).view(tv_more)
                    .listener(object : MenuPopup.OnItemSelectedListener {
                        override fun onSelect(pos: Int) {
                            when (pos) {
                                0 ->{
                                    CityAddActivity.start(getActivity())
                                }
                                1 ->{
                                    SimulateDialog.with(getActivity()).listener(object :
                                        SimulateDialog.OnItemSelectedListener {
                                        override fun onSelect(data: WeatherDec?, pos: Int) {
                                            data?.let {
                                                weatherView.setWeatherType(it.dayType,it.dec)
                                            }?: let{
                                                weatherView.setWeatherType(Data.dayType,dayText)
                                            }

                                        }
                                    }).show()
                                }
                                2 ->{
                                    AboutMeActivity.start(getActivity())
                                }
                            }

                        }
                    }).show()
            }
        }
    }

    //更改背景
    fun updateBg() {
        if (Data.dayType == -1)
            return
        if (TextUtils.isEmpty(dayText))
            return
        weatherView.setWeatherType(Data.dayType,dayText)
        ctl_main.setTitle(PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_NAME,"北京"))
    }


    @SuppressLint("SetTextI18n")
    override fun nowSuccess(data: Now, updataTime: String) {

        tv_updataTime.setText("上次更新时间:" + DateUtil.strToDateHHmm(updataTime))
        tv_temp.setText(data.temp)
        tv_text.setText(data.text)
        dayText = data.text
        updateBg()
    }

    override fun nowFailed() {
        showToast("城市有误")
    }

    override fun query24hSuccess(data: MutableList<Hourly>) {
        hideLoading()
        ll_view_24h.visibility= View.VISIBLE
        mD24hAdapter.setNewData(data)
        // rv_24h.scrollToPosition(6)
    }

    override fun query24hFailed() {
        hideLoading()
    }

    override fun query7dSuccess(data: MutableList<Daily>) {
        hideLoading()
        ll_view_7d.visibility= View.VISIBLE
        mDayAdapter.setNewData(data)
        tv_tempMax.setText( data[0].tempMax)
        tv_tempMin.setText(data[0].tempMin)
        val mNowTime = DateUtil.getNowDate(DateUtil.HHmm).toString()
        val sunrise = data[0].sunrise//日出
        val sunset = data[0].sunset//日落
        val moonrise = data[0].moonrise//月出
        val moonset = data[0].moonset//月落
        val moonPhase = data[0].moonPhase//
     // 04:53--19:30--16:49--03:57--20:45===0
        //L.i(DateUtil.timeCompare(mNowTime, sunset))
       // L.i(DateUtil.timeCompare(sunrise, mNowTime))
        //如果现在时间在日出和月落之间就是白天
        if (DateUtil.timeCompare(sunrise, mNowTime) && DateUtil.timeCompare(mNowTime, sunset)) {
            Data.dayType = 0
            sunDayView.setTime(sunrise, sunset, mNowTime, moonPhase)
        } else {
            Data.dayType = 1
            sunDayView.setTime(moonrise, moonset, mNowTime, moonPhase)
        }
        L.i("$sunrise--$sunset--$moonrise--$moonset--$mNowTime===$Data.dayType")
        ll_view_sun.visibility= View.VISIBLE
        updateBg()

    }

    override fun query7dFailed() {

    }

    override fun nowAirSuccess(data: NowAir) {
        ll_view_air.visibility= View.VISIBLE
        val mAirdatas: MutableList<Air> = ArrayList()
        mAirdatas.add(Air("PM2.5", data.pm2p5))
        mAirdatas.add(Air("NO2", data.no2))
        mAirdatas.add(Air("NO2", data.so2))
        mAirdatas.add(Air("O3", data.o3))
        mAirdatas.add(Air("CO", data.co))
        mAirAdapter.setNewData(mAirdatas)
        cp_air.setHint(data.category)
        cp_air.setValue(data.aqi.toFloat())

    }

    override fun nowAirFailed() {

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        val Offset = Math.abs(verticalOffset)
        val mAlpha = 1 - Offset.toFloat() / 210f
        ll_now.alpha = mAlpha
        //  L.i(""+Offset +"====="+mAlpha)
        if (cp_air.isCover()) {
            cp_air.startAnimation()
        }


    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            true
        } else super.dispatchKeyEvent(event)
    }


}