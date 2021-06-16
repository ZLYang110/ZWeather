package com.zlyandroid.weather.core.amap

import android.content.Context
import android.content.pm.PackageManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.app.AppConfig
import com.zlyandroid.weather.event.LocationCityEvent
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * @author zhangliyang
 * @date 2021/3/16
 * des：高德地图定位
 */

object AMapLocationEngine : AMapLocationListener {

    //声明mlocationClient对象
   private var mlocationClient: AMapLocationClient? = null
    //声明mLocationOption对象
    private var mLocationOption: AMapLocationClientOption? = null



    fun init(){

        sHA1(AppConfig.getContext())?.let { L.i("sHA -=== "+it) }

        mlocationClient = AMapLocationClient(AppConfig.getContext())
        //初始化定位参数
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption!!.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        //设置定位间隔,单位毫秒,默认为2000ms
         mLocationOption!!.setInterval(5 * 60 * 1000)
        //设置是否单次定位
        mLocationOption!!.setOnceLocation(true)
       //设置定位监听
        mlocationClient!!.setLocationListener(this)
      //设置定位参数
        mlocationClient!!.setLocationOption(mLocationOption)
        //启动定位
        mlocationClient!!.startLocation()

    }
    fun nowLocation(){
        mlocationClient!!.startLocation()
    }

    fun stopMap(){
        mlocationClient!!.stopLocation()
        mlocationClient!!.onDestroy()
    }

    override fun onLocationChanged(p0: AMapLocation) {
        L.i(p0)
        LocationCityEvent.postLocationCityEvent(p0.district)
    }


    //获取HA1
    fun sHA1(context: Context): String? {
        try {
            val info = context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_SIGNATURES)
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance("SHA1")
            val publicKey = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                        .toUpperCase(Locale.US)
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            val result = hexString.toString()
            return result.substring(0, result.length - 1)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }
}