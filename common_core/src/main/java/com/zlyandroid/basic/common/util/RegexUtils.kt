package com.zlyandroid.basic.common.util

import java.util.regex.Pattern


/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 *  desc: 字符串正则匹配
 */

object RegexUtils {

    const val PASSWORD_MIN_LENGTH = 6
    const val PASSWORD_MAX_LENGTH = 18

    private const val REGEX_E_MAIL =
        "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
    private const val REGEX_PHONE = "^(1[3456789][0-9])\\d{8}"
    private const val REGEX_PASSWORD_NUN_AND_EN =
        "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{$PASSWORD_MIN_LENGTH,$PASSWORD_MAX_LENGTH}$"
    private const val REGEX_PASSWORD_NUN_OR_EN =
        "^[a-z0-9A-Z]{$PASSWORD_MIN_LENGTH,$PASSWORD_MAX_LENGTH}$"
    private const val REGEX_ID_NUM = "^\\d{15}|\\d{18}|\\d{17}(\\d|X|x)"
    private const val REGEX_PASSWORD_LENGTH =
        "^.{$PASSWORD_MIN_LENGTH,$PASSWORD_MAX_LENGTH}$"
    private const val REGEX_PHONE_LENGTH = "^.{11}$"
    private const val REGEX_PHONE_HIDE = "(\\d{3})\\d{4}(\\d{4})"
    private const val REGEX_E_MAIL_HIDE = "(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)"

    /**
     * 邮箱格式是否正确
     *
     * @param email String
     * @return boolean
     */
    fun matchEmail(email: String?): Boolean {
        return match(email, REGEX_E_MAIL)
    }

    /**
     * 手机号格式是否正确
     *
     * @param phone String
     * @return boolean
     */
    fun matchPhone(phone: String?): Boolean {
        return match(phone, REGEX_PHONE)
    }

    /**
     * 手机号长度是否正确
     *
     * @param phone String
     * @return boolean
     */
    fun matchPhoneLength(phone: String?): Boolean {
        return match(phone, REGEX_PHONE_LENGTH)
    }

    /**
     * 手机号用****号隐藏中间数字
     *
     * @param phone String
     * @return String
     */
    fun hidePhone(phone: String): String? {
        return phone.replace(REGEX_PHONE_HIDE.toRegex(), "$1****$2")
    }

    /**
     * 邮箱用****号隐藏前面的字母
     *
     * @param email String
     * @return String
     */
    fun hideEmail(email: String): String? {
        return email.replace(REGEX_E_MAIL_HIDE.toRegex(), "$1****$3$4")
    }

    /**
     * 密码格式是否正确
     *
     * @param psw String
     * @return boolean
     */
    fun matchPassword(psw: String?): Boolean {
        return match(psw, REGEX_PASSWORD_NUN_OR_EN)
    }

    /**
     * 密码长度是否正确
     *
     * @param psw String
     * @return boolean
     */
    fun matchPasswordLength(psw: String?): Boolean {
        return match(psw, REGEX_PASSWORD_LENGTH)
    }

    /**
     * 身份证号格式是否正确
     *
     * @param id String
     * @return boolean
     */
    fun matchIdNum(id: String?): Boolean {
        return match(id, REGEX_ID_NUM)
    }

    /**
     * 字符串正则匹配
     *
     * @param s     待匹配字符串
     * @param regex 正则表达式
     * @return boolean
     */
    fun match(s: String?, regex: String?): Boolean {
        if (s == null) {
            return false
        }
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(s)
        return matcher.matches()
    }
}