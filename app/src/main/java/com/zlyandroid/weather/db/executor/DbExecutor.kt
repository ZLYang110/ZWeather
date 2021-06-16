package com.zlyandroid.weather.db.executor

import kotlinx.coroutines.*

/**
 * @author zhangliyang
 * @date 2020/12/15
 * GitHub: https://github.com/ZLYang110
 * desc:
 */

open class DbExecutor : CoroutineScope by MainScope() {

    fun destroy() {
        cancel()
    }

    fun <T> execute(runnable: suspend () -> T,success: ((t: T) -> Unit)? = null, error: ((e: Throwable) -> Unit)? = null) {
        launch(CoroutineExceptionHandler { _, _ -> }) {
            val result: T = withContext(Dispatchers.IO) {
                runnable.invoke()
            }
            success?.invoke(result)
        }.invokeOnCompletion {
            it?.let {
                error?.invoke(it)
            }
        }
    }
}