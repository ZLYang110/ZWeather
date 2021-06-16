package com.zlyandroid.weather.util

import android.content.Context
import android.util.Base64
import java.io.*

class DataUtils<T> {

    fun getJsonDataFromRaw(context: Context, fileName: Int): String? {
        val jsonString: String
        try {
            jsonString = context.resources.openRawResource(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    /**
     * User 转 String
     * @param user
     * @return
     * @throws IOException
     */
    @Throws(IOException::class)
      fun translateUserInfoTOString(data: T): String {
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(data)
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT)
    }

    /**
     * String 转 User
     * @param userStr
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Throws(IOException::class, ClassNotFoundException::class)
      fun translateStringTOUserInfo(userStr: String ): T  {
        val base64Bytes = Base64.decode(userStr, Base64.DEFAULT)
        val bis = ByteArrayInputStream(base64Bytes)
        val ois = ObjectInputStream(bis)
        return ois.readObject() as T
    }
}