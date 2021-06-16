package com.zlyandroid.weather.http.response

import android.text.TextUtils
import java.util.*
import kotlin.collections.ArrayList

open class ResponseBean {
    var code: Int = 0
    var errorCode: Int = 0
    var errorMsg: String = ""
    var updateTime: String = ""

   fun getRCode():Int{
       if(code != 0){
           return code
       }
       if(errorCode != 0){
           return errorCode
       }
       return errorCode
   }
    fun getRMsg():String{
        if(TextUtils.isEmpty(errorMsg)){
            return "失败"
        }
        if (!errorMsg.isNotEmpty())
            return errorMsg
        return "成功"
    }

}