package com.zlyandroid.basic.common.util.log

import android.os.Environment
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat

/**
 * @author zhangliyang
 * @date 2020/11/13
 * GitHub: https://github.com/ZLYang110
 */
object FileLog {

    var yyyy_mm_dd = "yyyy-MM-dd"
    var yyyymmdd = "yyyyMMdd"
    var yyyymmddhhmmss = "yyyyMMddHHmmss"
    var yyyymmdd_hh_mm_ss = "yyyyMMdd_HH:mm:ss"

    /**
     * 保存文件地址
     */
    var SAVE_FILE_DIR =  Environment.getExternalStorageDirectory().absolutePath + File.separator + "log"

    /**
     * 操作日志名字拼接
     */
    var SAVE_FILE_NAME = "log_"

    /**
     * 日志的保存的类型
     */
    const val SAVE_FILE_TYPE = ".log"


    var FILE_COUNT = 1
    var TOTAL_SIZE = 2

    var DEFAULT_MAX_TOTAL_SIZE = 32 * 1024 * 1024 // 32mb
        .toLong()
    var DEFAULT_MAX_FILE_COUNT = 24 * 7 // ~7 days of restless logging


    /**
     * 保存到文件，指定文件名
     */
    fun printFile(
        tag: String?,
        targetDirectory: File?,
        fileName: String?,
        headString: String?,
        msg: String
    ) {
        var fileName = fileName
        fileName = fileName ?: SAVE_FILE_NAME + getStringTime(yyyymmddhhmmss) + SAVE_FILE_TYPE
        if (targetDirectory?.let { save(it, fileName!!, msg) }!!) {
            Log.d(tag,
                headString + " save log success ! location is >>>" + targetDirectory.absolutePath + "/" + fileName)
        } else {
            Log.e(tag, headString + "save log fails !")
        }
    }

    private fun save(dic: File, fileName: String, msg: String): Boolean {
        val file = File(dic, fileName)
        if (!ensureDir(dic)) {
            dic.mkdirs()
        }
        return try {
            val outputStream: OutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(outputStream, "UTF-8")
            outputStreamWriter.write(msg)
            outputStreamWriter.flush()
            outputStream.close()
            true
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



    @JvmStatic
    fun getFileName(): String? {
        return SAVE_FILE_NAME + getStringTime(yyyy_mm_dd) + SAVE_FILE_TYPE
    }


    /**
     * 判断文件是否存在
     */
    @JvmStatic
    fun ensureDir(dir: File): Boolean {
        if (dir.exists()) {
            if (dir.isDirectory) {
                return true
            }
            if (!dir.delete()) {
                Log.i("FLUtil", "failed to delete file that occupies log dir path: [" +
                        dir.absolutePath + "]")
                return false
            }
        }
        if (!dir.mkdir()) {
            Log.i("FLUtil", "failed to create log dir: [" + dir.absolutePath + "]")
            return false
        }
        return true
    }


    @JvmStatic
    fun ensureFile(logFileDir: File) {
        check(!(logFileDir.exists() && !logFileDir.isFile() && !logFileDir.delete())) {
            "failed to delete directory that occupies log file path: [" +
                    logFileDir.getAbsolutePath() + "]"
        }
    }


    /**
     * 描述：Date类型转化为String类型.
     *
     * @param format the format
     * @return String String类型日期时间
     */
    open fun getStringTime(format: String?): String {
        val mSimpleDateFormat = SimpleDateFormat(format)
        lateinit var strDate: String
        try {
            strDate = mSimpleDateFormat.format(System.currentTimeMillis())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return strDate
    }
}