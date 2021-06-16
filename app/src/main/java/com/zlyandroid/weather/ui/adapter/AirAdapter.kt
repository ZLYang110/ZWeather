package com.zlyandroid.weather.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zlyandroid.weather.R
import com.zlyandroid.weather.http.Air
import com.zlyandroid.weather.http.Daily
import com.zlyandroid.weather.http.Hourly
import com.zlyandroid.weather.util.DateUtil
import com.zlyandroid.weather.util.IconUtils

/**
 * @author zhangliyang
 * @date 2021/1/18
 * GitHub: https://github.com/ZLYang110
 * desc: 空气质量
 */

class AirAdapter(data: MutableList<Air>) : BaseQuickAdapter<Air, BaseViewHolder>(R.layout.item_air,data) {

    override fun convert(helper: BaseViewHolder, item: Air) {
        helper.setText(R.id.tv_title, item.title)
            .setText(R.id.tv_api,item.aqi)
    }
}