package com.zlyandroid.basic.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.zlyandroid.basic.common.AppCommon
import java.util.*
/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 */
object DeviceIdUtils {

    private const val KEY_ID = "id"
    private const val SP_NAME = "device_id"

    @SuppressLint("HardwareIds")
    fun getId(): String {
        synchronized(DeviceIdUtils::class.java) {
            val sp = AppCommon.getApplication()
                .getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            var deviceId = sp.getString(KEY_ID, null)
            if (!TextUtils.isEmpty(deviceId)) {
                return deviceId!!
            }
            var id: String =  getAndroidId() + getSerial()
            if (TextUtils.isEmpty(id)) {
                id = UUID.randomUUID().toString()
            }
            deviceId = MD5Coder.encode(id)
            sp.edit().putString(KEY_ID, deviceId).apply()
            return deviceId
        }
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(): String? {
        return Settings.Secure.getString(
            AppCommon.getApplication().contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    @SuppressLint("HardwareIds")
    fun getSerial(): String? {
        return Build.SERIAL
    }
}