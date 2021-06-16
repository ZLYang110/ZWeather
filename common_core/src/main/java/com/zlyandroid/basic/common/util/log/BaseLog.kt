package com.zlyandroid.basic.common.util.log

import android.util.Log
import com.zlyandroid.basic.common.util.log.LogLevel.A
import com.zlyandroid.basic.common.util.log.LogLevel.D
import com.zlyandroid.basic.common.util.log.LogLevel.E
import com.zlyandroid.basic.common.util.log.LogLevel.I
import com.zlyandroid.basic.common.util.log.LogLevel.V
import com.zlyandroid.basic.common.util.log.LogLevel.W

/**
 * @author zhangliyang
 * @date 2020/11/13
 * GitHub: https://github.com/ZLYang110
 */
object BaseLog {
    private const val MAX_LENGTH = 4000

    fun printDefault(type: Int, tag: String?, msg: String?) {
        var index = 0
        val length = msg?.length
        val countOfSub = length?.div(MAX_LENGTH)
        if (countOfSub != null) {
            if (countOfSub > 0) {
                for (i in 0 until countOfSub) {
                    val sub = msg.substring(index, index +  MAX_LENGTH)
                    printSub(type, tag, sub)
                    index +=  MAX_LENGTH
                }
                printSub(type, tag, msg.substring(index, length))
            } else {
                printSub(type, tag, msg)
            }
        }
    }

    private fun printSub(type: Int, tag: String?, sub: String?) {
        when (type) {
            V -> Log.v(tag, sub!!)
            D -> Log.d(tag, sub!!)
            I -> Log.i(tag, sub!!)
            W -> Log.w(tag, sub!!)
            E -> Log.e(tag, sub!!)
            A -> Log.wtf(tag, sub)
        }
    }
}