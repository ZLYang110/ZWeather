package com.zlyandroid.weather.ui.presenter


import android.app.Activity
import android.text.TextUtils
import com.zlyandroid.basic.common.listener.SimpleCallback
import com.zlyandroid.basic.common.listener.SimpleListener
import com.zlyandroid.basic.common.mvp.BasePresenter
import com.zlyandroid.basic.common.util.PreUtils
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.AppCon
import com.zlyandroid.weather.core.Constants
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.db.executor.CityListExecutor
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.http.response.go
import com.zlyandroid.weather.ui.bean.Now
import com.zlyandroid.weather.ui.model.WeatherModel
import com.zlyandroid.weather.ui.view.CityListView
import com.zlyandroid.weather.util.DataUtils
import com.zlyandroid.weather.util.JsonUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * @author zhangliyang
 * @date 2020/11/14
 * GitHub: https://github.com/ZLYang110
 */
class CityListPresenter : BasePresenter<CityListView>() {
    var mModel = WeatherModel()
    //城市列表
    private var mCityListExecutor: CityListExecutor? = null

    override fun attachView(view: CityListView) {
        super.attachView(view)

        mCityListExecutor = CityListExecutor()
    }
    override fun detachView() {
        super.detachView()
        mCityListExecutor?.destroy()
    }

    /**
     * 获取已添加到城市管理的城市列表
     * @param
     * */
    var dataList:MutableList<CityModel>?=null
    var index=0
    fun getSelectedCity(isUpdata: Boolean) {
        mCityListExecutor?.findBySelect( true,success = object : SimpleCallback<List<CityModel>> {

            override fun onResult(data: List<CityModel>) {
                mView?.getCityListSuccess(data)
                if(isUpdata){
                      dataList = data.toMutableList()
                    if(!TextUtils.isEmpty(Data.locationAddress)){
                        Data.locationNow?.run {
                            var cityModel  =CityModel(Data.locationAddressID,Data.locationAddress,DataUtils<Now>().translateUserInfoTOString(this),false,"",0,System.currentTimeMillis())
                            dataList!!.add(0,cityModel)
                        }
                    }
                    dataList?.let {
                        if(it.size>0){
                            now(it[index])
                        }
                    }
                }
            }
        }, error = object : SimpleListener {
            override fun onResult() {
                mView?.getCityListFailed()
            }
        })
    }
    /**
     *   更新实时天气
     * */
    fun now(data: CityModel) {
        index+=1
        mModel.nowWeather(data.cityId, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
               // L.i(data.cityName+it.now.toJson())
                data.run {
                    cityNow = DataUtils<Now>().translateUserInfoTOString(it.now)
                    updateTime = it.updateTime
                    updataDBNow(this)
                }
                dataList?.let {
                    if(index < dataList!!.size){
                        now(dataList!![index])
                    }
                }?:let {
                    mView?.updataCitySuccess(data)
                }
            }, onError = {
                mView?.getCityListFailed()
            })
    }



    /**
     *   城市管理删除城市
     * */
    fun removeSelectedCity(cityName:String) {
        mCityListExecutor?.findByName(cityName, success = object : SimpleCallback<CityModel> {
            override fun onResult(data: CityModel) {
                L.i(data)
                data?.run {
                    isSelect = false
                    updataDBNow(this)
                }
            }
        }, error = object : SimpleListener {
            override fun onResult() {
                //mView?.nowFailed()

            }
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
    * 获取到所有城市列表
    * */
    fun getList(activity: Activity) {
        mCityListExecutor?.getList( success = object : SimpleCallback<List<CityModel>> {

            override fun onResult(data: List<CityModel>) {
                if(data.size==0){
                    setData(activity)
                }else{
                    mView?.getCityListSuccess(data)
                }
            }
        }, error = object : SimpleListener {
            override fun onResult() {
                mView?.getCityListFailed()
            }
        })
    }

    /**
     * 数据库没有数据，添加默认城市
     * */
   var cityList: MutableList<CityModel> = ArrayList()
   private fun setData(activity: Activity){
       cityList = ArrayList()
       PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_ID,101010100)
       PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_NAME,"北京")
       PreUtils.getInstance().save(AppCon.CITY_KEY.SP_CITY_LOACTION,false)
       try {
           var  jsonString = JsonUtils.getJsonDataFromRaw(activity,R.raw.city)
           val obj = JSONObject(jsonString)
           var cityJson: JSONArray =obj.getJSONArray("city")
           var cityModel :CityModel
           for (i in 0 until cityJson.length()) {
               val userDetail = cityJson.getJSONObject(i)
               L.i(userDetail)
               cityModel  =CityModel( userDetail.getString("areaId"),userDetail.getString("areaName"),null,false,"",0,System.currentTimeMillis())
               cityList.add(cityModel)
           }
       }

       catch (e: JSONException) {
           e.printStackTrace()
       }
       mCityListExecutor?.add(*cityList.toTypedArray(), success = object : SimpleCallback<List<CityModel>> {

           override fun onResult(data: List<CityModel>) {
               L.i(data)
               mView?.getCityListSuccess(data)
           }
       }, error = object : SimpleListener {
           override fun onResult() {
               mView?.getCityListFailed()
           }
       })
    }
    /**
     * 清空数据库
     * */
    fun removeAll() {
        mCityListExecutor?.removeAll( success = object : SimpleListener{

            override fun onResult() {
                cityList = ArrayList()
                mView?.removeSuccess()
            }
        }, error = object : SimpleListener {
            override fun onResult() {
                mView?.removeFailed()
            }
        })
    }

    /**
    * 初始话
    * */
    fun getDBNow(cityName: String) {

        mCityListExecutor?.findByName(cityName, success = object : SimpleCallback<CityModel> {
            override fun onResult(data: CityModel) {
                if(TextUtils.isEmpty(data.cityId)){
                    lookup(cityName)
                }else{
                    now(data.cityId)
                }
            }
        }, error = object : SimpleListener {
            override fun onResult() {
                lookup(cityName)
            }
        })
    }
    fun lookup(location: String ) {

        mModel.lookup(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                L.i(it.location)
                if (it.location.size > -1) {
                    var data = it.location[0]
                    now(data.id)

                }
            }, onError = {

            })
    }
    fun now(location: String) {
        mModel.nowWeather(location, Constants.weatherKey)
            .go(mModel, mView, isShowLoading = false, onSuccess = {
                //L.i(it.now.toJson())
                Data.showNow = it.now
                Data.showUpdateTime = it.updateTime
            }, onError = {

            })
    }

}