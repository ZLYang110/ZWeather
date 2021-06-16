@file:Suppress("DEPRECATION")

package com.zlyandroid.basic.common.util.log

import android.os.Environment
import android.os.Environment.*
import java.io.File
/**
 * @author zhangliyang
 * @date 2020/11/13
 * GitHub: https://github.com/ZLYang110
 */
object L {

    var saveFileDir =
        getExternalStorageDirectory().absolutePath + File.separator + "log"
    var saveFileName = "log_"
    private var isInit = false //是否初始化
    var isDebug = true //是否输出日志

    /**
    * 是否实时保存log
     * 如果是 :true
     * 保存logd打印输出的所有内容（I，V，D，W，E，json，xml）
    * */
    var isSaveLog = false // 保存I，V，D，W，E

    /**
     * 初始化日志打印
     *
     * @param TAG     日志输出标签
     * @param isDebug 是否输出日志
     */
    fun init(TAG: String, isbug: Boolean) {
        isDebug = isbug
        isInit = true
        FileLog.SAVE_FILE_DIR = saveFileDir;
        FileLog.SAVE_FILE_NAME = saveFileName;
        ZLog.init(isDebug,isSaveLog, TAG)
    }

    fun v(objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.v(objectMsg)
    }

    fun v(tag: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.d(tag, objectMsg)
    }

    fun d(objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.d(objectMsg)
    }

    fun d(tag: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.d(tag, objectMsg)
    }

    fun e(objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.e(objectMsg)
    }

    fun w(objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.w(objectMsg)
    }

    fun w(tag: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.w(tag, objectMsg)
    }

    fun i(objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.i(objectMsg)
    }

    fun i(tag: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.i(tag, objectMsg)
    }

    fun json(objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.json(objectMsg)
    }

    fun json(tag: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.json(tag, objectMsg)
    }

    fun xml(objectMsg: String) {
        if (!isInit) {
            throwException()
        }
        ZLog.xml(objectMsg)
    }

    fun xml(tag: String, objectMsg: String) {
        if (!isInit) {
            throwException()
        }
        ZLog.xml(tag, objectMsg)
    }
    fun file(  objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.file(File(saveFileDir), objectMsg)
    }
    fun file(targetDirectory: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.file(File(targetDirectory), objectMsg)
    }

    fun file(tag: String?, targetDirectory: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.file(tag, File(targetDirectory), objectMsg)
    }

    /**
     * 保存指定内容到文件
     * 使用每次调用都会生成一个新的文件。
     * 每次调用都会把objectMsg生成一个新的文件。
     * 文件命名：log_202011181423.log
     * 也可根据传入文件名 fileName 自定义。如果不传入就会使用如上述文件
     * targetDirectory 保存路径 。如果不传入 使用 saveFileDir
     * */
    fun file(tag: String, targetDirectory: String, fileName: String, objectMsg: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.file(tag, File(targetDirectory), fileName, objectMsg)
    }

    /**
     * 实时保存日志
     * 使用此方法打印日志多次调用保存在同一个文件里面
     *  文件命名：log_20201118.log
     * 此方法和isSaveLog 不冲突
     * 如果设置 isSaveLog = true ，在调用此方法，文本保存在同一个文件夹
     *
     * saveFileName 设置此方法的保存文件名
     * saveFileDir 设置此方法的保存路径
     * */
    fun writeLog( tag: String?, args: Any) {
        if (!isInit) {
            throwException()
        }
        ZLog.writeLog(tag,args)
    }

    private fun throwException() {
        throw NullPointerException("日志未初始化,请调用init()方法初始化后再试!")
    }
}