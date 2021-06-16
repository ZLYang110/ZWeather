package com.zlyandroid.basic.common.http.exception

/**
 * @author zhangliyang
 * @date 2018/6/6
 * @desc
 */
class ApiException : RuntimeException {

    private var code: Int? = null

    constructor(throwable: Throwable, code: Int) : super(throwable) {
        this.code = code
    }

    constructor(message: String) : super(Throwable(message))
}