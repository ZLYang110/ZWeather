package com.zlyandroid.weather.db.executor

import com.zlyandroid.basic.common.listener.SimpleCallback
import com.zlyandroid.basic.common.listener.SimpleListener
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.db.ApiDb.db
import com.zlyandroid.weather.db.model.CityModel

/**
 * @author zhangliyang
 * @date 2021/5/19
 * GitHub: https://github.com/ZLYang110
 * desc:
 */

class CityListExecutor : DbExecutor() {

    fun add(vararg cityModel: CityModel,
            success: SimpleCallback<List<CityModel>>? , error: SimpleListener? ) {
        execute({
            db().CityListDao().insert(*cityModel)
            db().CityListDao().findAll()
        }, {
            success?.onResult(it)
        }, {
            L.i(it.localizedMessage)
            error?.onResult()
        })
    }

    fun updata(model: CityModel, success: SimpleListener, error: SimpleListener) {
        execute({
            db().CityListDao().updata(model)
        }, {
            success.onResult()
        }, {
            error.onResult()
        })
    }
    fun remove(cityName: String, success: SimpleListener, error: SimpleListener) {
        execute({
            db().CityListDao().delete(cityName)
        }, {
            success.onResult()
        }, {
            error.onResult()
        })
    }

    fun removeAll(success: SimpleListener, error: SimpleListener) {
        execute({
            db().CityListDao().deleteAll()
        }, {
            success.onResult()
        }, {
            error.onResult()
        })
    }
    fun findByName(cityName: String, success: SimpleCallback<CityModel>, error: SimpleListener) {
        execute({
            db().CityListDao().findById(cityName)
        }, {
            success.onResult(it)
        }, {
            error.onResult()
        })
    }
    fun findBySelect(isSelect: Boolean, success: SimpleCallback<List<CityModel>>, error: SimpleListener) {
        execute({
            db().CityListDao().findBySelect(isSelect)
        }, {
            success.onResult(it)
        }, {
            error.onResult()
        })
    }
    fun getList(from: Int, count: Int, success: SimpleCallback<List<CityModel>>, error: SimpleListener) {
        execute({
            db().CityListDao().findAll(from, count)
        }, {
            success.onResult(it)
        }, {
            error.onResult()
        })
    }

    fun getList(success: SimpleCallback<List<CityModel>>, error: SimpleListener) {
        execute({
            db().CityListDao().findAll()
        }, {
            success.onResult(it)
        }, {
            error.onResult()
        })
    }
}