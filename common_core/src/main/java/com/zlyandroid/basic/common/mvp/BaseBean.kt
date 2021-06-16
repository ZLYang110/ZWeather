package com.zlyandroid.basic.common.mvp

import com.google.gson.Gson
import com.zlyandroid.basic.common.util.JsonFormatUtils
import java.io.Serializable

open class BaseBean : Serializable {

    fun toJson(): String  {
        return Gson().toJson(this)
    }

    fun toFormatJson(): String  {
        return JsonFormatUtils.format(toJson())
    }
}