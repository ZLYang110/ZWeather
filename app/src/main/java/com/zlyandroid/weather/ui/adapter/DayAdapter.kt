package com.zlyandroid.weather.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zlyandroid.weather.R
import com.zlyandroid.weather.http.Daily
import com.zlyandroid.weather.http.Hourly
import com.zlyandroid.weather.util.DateUtil
import com.zlyandroid.weather.util.IconUtils

/**
 * @author zhangliyang
 * @date 2021/1/18
 * GitHub: https://github.com/ZLYang110
 * desc: 未来七天的情况
 */

class DayAdapter(data: MutableList<Daily>) : BaseQuickAdapter<Daily, BaseViewHolder>(R.layout.item_day,data) {

    override fun convert(helper: BaseViewHolder, item: Daily) {
        helper.setText(R.id.tv_date, item.fxDate)
            .setImageResource(R.id.iv_w, IconUtils.getDayIconDark(item.iconDay))
            .setText(R.id.tv_tempMax,item.tempMax+"°C")
            .setText(R.id.tv_tempMin,item.tempMin+"°C")
    }
}