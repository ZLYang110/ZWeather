package com.zlyandroid.weather.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.SwipeLayout.SwipeListener
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.Data
import com.zlyandroid.weather.db.model.CityModel
import com.zlyandroid.weather.ui.bean.Now
import com.zlyandroid.weather.util.DataUtils
import com.zlyandroid.weather.widget.weather.WeatherUtil
import java.util.*

/**
 * @author zhangliyang
 * @date 2021/6/5
 * GitHub: https://github.com/ZLYang110
 * desc:
 */

class CityAdapter(data: MutableList<CityModel>) : BaseQuickAdapter<CityModel, BaseViewHolder>(R.layout.item_city,data) {

    private val mUnCloseList: MutableList<SwipeLayout> =  ArrayList()
    override fun convert(helper: BaseViewHolder, item: CityModel) {
        helper.setVisible(R.id.iv_location, false)

        item.cityNow?.let {
            val now = DataUtils<Now>().translateStringTOUserInfo(it)
            helper.setText(R.id.tv_temp, now.temp)
            helper.setText(R.id.tv_text, now.text)
            helper.setBackgroundRes(R.id.rl_top,WeatherUtil.getWeatherTypeBg(Data.dayType,now.text))
        }
        helper.setText(R.id.tv_cityName, item.cityName)


        val sl = helper.getView<SwipeLayout>(R.id.sl)
        sl.addSwipeListener(object : SwipeListener {
            override fun onStartOpen(layout: SwipeLayout) {
                closeAll(layout)
            }

            override fun onOpen(layout: SwipeLayout) {
                mUnCloseList.add(layout)
            }

            override fun onStartClose(layout: SwipeLayout) {}
            override fun onClose(layout: SwipeLayout) {
                mUnCloseList.remove(layout)
            }
            override fun onUpdate(layout: SwipeLayout, leftOffset: Int, topOffset: Int) {}
            override fun onHandRelease(layout: SwipeLayout,xvel: Float,yvel: Float) {}
        })

        helper.addOnClickListener(R.id.rl_top, R.id.iv_delete)

        if(helper.layoutPosition == 0){
            if(!TextUtils.isEmpty(Data.locationAddress)){
                sl.isRightSwipeEnabled = false
                helper.setVisible(R.id.iv_location, true)
            }
        }
    }




    fun closeAll(layout: SwipeLayout?) {
        for (swipeLayout in mUnCloseList) {
            if (layout === swipeLayout) {
                continue
            }
            if (swipeLayout.openStatus != SwipeLayout.Status.Open) {
                continue
            }
            swipeLayout.close()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object :  RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    closeAll(null)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView,dx: Int,dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }


}