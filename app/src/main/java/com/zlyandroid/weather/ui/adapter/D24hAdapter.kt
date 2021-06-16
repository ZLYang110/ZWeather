package com.zlyandroid.weather.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zlyandroid.weather.R
import com.zlyandroid.weather.http.Hourly
import com.zlyandroid.weather.util.DateUtil
import com.zlyandroid.weather.util.IconUtils

/**
 * @author zhangliyang
 * @date 2021/1/18
 * GitHub: https://github.com/ZLYang110
 * desc:当天24h天气情况
 */

class D24hAdapter(data: MutableList<Hourly>) : BaseQuickAdapter<Hourly, BaseViewHolder>(R.layout.item_hour,data) {

    override fun convert(helper: BaseViewHolder, item: Hourly) {
        helper.setText(R.id.tv_hour, DateUtil.strToDateHHmm(item.fxTime))
            .setImageResource(R.id.iv_w, IconUtils.getDayIconDark(item.icon))
            .setText(R.id.tv_temp,item.temp+"°C")
    }
}