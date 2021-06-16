package com.zlyandroid.basic.common.util

import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and


/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 */
object MD5Coder {

    fun encode(string: String, slat: String): String {
        return encode(string + slat)
    }

    /**
     * MD5加密
     */
    fun encode(string: String): String {
        return if (TextUtils.isEmpty(string)) {
            ""
        } else try {
            val md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(string.toByteArray())
            val result = StringBuilder()
            for (b in bytes) {
                var temp = Integer.toHexString((b and 0xff.toByte()).toInt())
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result.append(temp)
            }
            result.toString()
        } catch (e: NoSuchAlgorithmException) {
            ""
        }
    }

    /**
     * MD5加密
     */
    fun encode(string: String, times: Int): String {
        if (TextUtils.isEmpty(string)) {
            return ""
        }
        if (times <= 1) {
            return encode(string)
        }
        var md5 = encode(string)
        for (i in 0 until times - 1) {
            md5 = encode(md5)
        }
        return md5
    }

    fun encode2(string: String): String {
        return encode(string, 2)
    }

    fun encode(file: File?): String {
        if (file == null || !file.isFile || !file.exists()) {
            return ""
        }
        var `in`: FileInputStream? = null
        val result = StringBuilder()
        val buffer = ByteArray(8192)
        var len: Int
        try {
            val md5 = MessageDigest.getInstance("MD5")
            `in` = FileInputStream(file)
            while (`in`.read(buffer).also { len = it } != -1) {
                md5.update(buffer, 0, len)
            }
            val bytes = md5.digest()
            for (b in bytes) {
                var temp = Integer.toHexString((b and 0xff.toByte()).toInt())
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result.append(temp)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result.toString()
    }
}