package com.zlyandroid.weather.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author zhangliyang
 * @date 2021/5/19
 * GitHub: https://github.com/ZLYang110
 * desc:城市列表
 */

@Entity
data class CityModel(
    var cityId: String,
    val cityName: String,
    var cityNow: String?,//城市最近一次所查的天气
    var isSelect: Boolean,//是否被选中
    var updateTime: String,//更新天气的时间
    var addTime: Long,// 添加到城市管理的时间
    val time: Long
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}