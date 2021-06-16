package com.zlyandroid.weather.ui.presenter

import android.text.TextUtils
import com.zlyandroid.basic.common.listener.SimpleCallback
import com.zlyandroid.basic.common.listener.SimpleListener
import com.zlyandroid.basic.common.mvp.BasePresenter
import com.zlyandroid.basic.common.util.PreUtils
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.core.AppCon
import com.zlyandroid.weather.core.Constants
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.db.executor.CityListExecutor
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.http.response.go
import com.zlyandroid.weather.ui.bean.Now
import com.zlyandroid.weather.ui.model.WeatherModel
import com.zlyandroid.weather.ui.view.WeatherView
import com.zlyandroid.weather.util.DataUtils

/**
 * @author zhangliyang
 * @date 2021/1/14
 * GitHub: https://github.com/ZLYang110
 */
class WeatherPresenter : BasePresenter<WeatherView>() {
    var mModel = WeatherModel()
    var mCityModel: CityModel? = null//当前城市数据

    //城市列表
    private var mCityListExecutor: CityListExecutor? = null

    override fun attachView(view: WeatherView) {
        super.attachView(view)
        mCityListExecutor = CityListExecutor()
    }

    override fun detachView() {
        super.detachView()
        mCityListExecutor?.destroy()
    }

    /**
     * 根据城市名字查询数据库
     * */
    fun getDBNow(cityName: String) {
        var cityName = cityName
        L.i("getDBNow  -- 进来了 --" + cityName)
        if (cityName.lastIndexOf("市") > -1) {
            cityName = cityName.substring(0, cityName.lastIndexOf("市"))
        }
        L.i("getDBNow  -- 进来了 --" + cityName)
        mCityListExecutor?.findByName(cityName, success = object : SimpleCallback<CityModel> {
            override fun onResult(data: CityModel) {
                L.i(data)
                mCityModel = data
                if(TextUtils.isEmpty(data.cityId)){
                    lookup(cityName, false)
                }else{
                    PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_ID, data.cityId)
                    PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_NAME, data.cityName)
                    now(data.cityId)
                    query24h(data.cityId)
                    query7d(data.cityId)
                    nowAir(data.cityId)
                    data.cityNow?.let {
                        PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_TEMP, data.cityNow)
                        PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_UPDATATIME, data.updateTime)
                        val now = DataUtils<Now>().translateStringTOUserInfo(it)
                        mView?.nowSuccess(now, data.updateTime)
                    }
                }

            }
        }, error = object : SimpleListener {
            override fun onResult() {
                //mView?.nowFailed()
                L.i("qaaaaaaaaaaaaaaaaaaaaaaaa")
                lookup(cityName, true)
            }
        })
    }


    fun now(location: String) {
        mModel.nowWeather(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                L.i(it.now.toJson())
                var isLoaction = PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_LOACTION, false)
                if (isLoaction) {
                    Data.locationNow = it.now
                } else {
                    //添加的城市默认添加到城市管理
                    mCityModel?.run {
                        cityId = location
                        cityNow = DataUtils<Now>().translateUserInfoTOString(it.now)
                        updateTime = it.updateTime
                        isSelect = true
                        addTime = System.currentTimeMillis()
                        updataDBNow(this)
                    }
                }
                PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_TEMP, mCityModel?.cityNow)
                PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_UPDATATIME, it.updateTime)
                mView?.nowSuccess(it.now, it.updateTime)
            }, onError = {
                mView?.nowFailed()
            })
    }

    fun query24h(location: String) {
        mModel.query24h(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                //  L.i(it.hourly)
                mView?.query24hSuccess(it.hourly)
            }, onError = {
                mView?.query24hFailed()
            })
    }

    fun query7d(location: String) {
        mModel.query7d(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                //L.i(it.daily)
                mView?.query7dSuccess(it.daily)
            }, onError = {
                mView?.query7dFailed()
            })
    }

    //空气质量
    fun nowAir(location: String) {
        mModel.nowAir(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                L.i(it.now)
                mView?.nowAirSuccess(it.now)
            }, onError = {
                mView?.nowAirFailed()
            })
    }

    /**
     * 城市信息查询
     * @param location 需要查询的城市 汉字和拼音
     * @param isAdd 是否要添加数据库  如果在数据库没有查到该城市的数据，就需要添加到数据库方便下次查找
     * */
    fun lookup(location: String, isAdd: Boolean) {
        L.i(location)
        mModel.lookup(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                L.i(it.location)
                if (it.location.size > -1) {
                    var data = it.location[0]
                    var isLoaction = PreUtils.getInstance().get(AppCon.CITY_KEY.SP_CITY_LOACTION, false)
                    var cityModel: CityModel?=null
                    //如果是定位出来的城市，默认不添加到数据库中，如果不是定位的城市（用户自己手动輸入的城市），默认添加到城市管理中
                    if (isLoaction) {
                        Data.locationAddress = data.name
                        Data.locationAddressID = data.id
                       // cityModel = CityModel( data.id,data.name,null,false,"",0,System.currentTimeMillis())
                    } else{
                        cityModel = CityModel(data.id, data.name, null, true, "", System.currentTimeMillis(), System.currentTimeMillis() )
                    }
                    if(isAdd){
                        if (cityModel != null) {
                            addDBCity(cityModel)
                        }
                    }
                    PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_ID, data.id)
                    PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_NAME, data.name)
                    now(data.id)
                    query24h(data.id)
                    query7d(data.id)
                    nowAir(data.id)
                }
            }, onError = {
                mView?.nowFailed()
            })
    }


    /**
     * 更新数据库信息
     * */
    fun updataDBNow(model: CityModel) {
        mCityListExecutor?.updata(model, success = object : SimpleListener {
            override fun onResult() {
            }
        }, error = object : SimpleListener {
            override fun onResult() {
            }
        })
    }

    /**
     * 添加到数据库
     * */
    fun addDBCity(model: CityModel) {
        mCityListExecutor?.add(model, success = object : SimpleCallback<List<CityModel>> {
            override fun onResult(data: List<CityModel>) {
                mCityListExecutor?.findByName(
                    model.cityName,
                    success = object : SimpleCallback<CityModel> {
                        override fun onResult(data: CityModel) {
                            L.i(data)
                        }
                    },
                    error = object : SimpleListener {
                        override fun onResult() {
                            //mView?.nowFailed()
                            L.i("没有？")
                        }
                    })
            }
        }, error = object : SimpleListener {
            override fun onResult() {
            }
        })
    }
}