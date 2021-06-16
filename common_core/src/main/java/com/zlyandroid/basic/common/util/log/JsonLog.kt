package com.zlyandroid.basic.common.util.log

import android.util.Log
import com.zlyandroid.basic.common.util.log.ZLogUtil.printLine
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
/**
 * @author zhangliyang
 * @date 2020/11/16
 * GitHub: https://github.com/ZLYang110
 */
object JsonLog {
    fun printJson(type: Int, tag: String?, msg: String?, headString: String?) {
        lateinit var message: String
        if (msg != null) {
            message = try {
                if (msg.startsWith("{")) {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(ZLog.JSON_INDENT)
                } else if (msg.startsWith("[")) {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(ZLog.JSON_INDENT)
                } else {
                    msg
                }
            } catch (e: JSONException) {
                msg
            }
        }
        printLine(type, tag, true)
        message = headString + ZLog.LINE_SEPARATOR + message
        val lines = ZLog.LINE_SEPARATOR?.let { message.split(it).toTypedArray() }
        if (lines != null) {
            for (line in lines) {
                when (type) {
                    LogLevel.D -> Log.d(tag, "║ $line")
                    LogLevel.E -> Log.e(tag, "║ $line")
                    LogLevel.W -> Log.w(tag, "║ $line")
                    LogLevel.I -> Log.i(tag, "║ $line")
                }
            }
        }
        printLine(type, tag, false)
    }

    fun printJson(tag: String?, msg: String?, headString: String?) {

        lateinit var message: String
        if (msg != null) {
            message = try {
                if (msg.startsWith("{")) {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(ZLog.JSON_INDENT)
                } else if (msg.startsWith("[")) {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(ZLog.JSON_INDENT)
                } else {
                    msg
                }
            } catch (e: JSONException) {
                msg
            }
        }
        printLine(tag, true)
        message = headString + ZLog.LINE_SEPARATOR + message
        val lines = ZLog.LINE_SEPARATOR?.let { message.split(it).toTypedArray() }
        if (lines != null) {
            for (line in lines) {
                Log.d(tag, "║ $line")
            }
        }
        printLine(tag, false)
    }
}