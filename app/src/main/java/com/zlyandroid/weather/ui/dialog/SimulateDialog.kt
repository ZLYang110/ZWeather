package com.zlyandroid.weather.ui.dialog

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.R
import com.zlyandroid.weather.widget.weather.WeatherDec
import com.zlyandroid.weather.widget.weather.WeatherUtil
import com.zlylib.upperdialog.Upper
import com.zlylib.upperdialog.manager.Layer
import com.zlylib.upperdialog.utils.ResUtils
import com.zlylib.upperdialog.view.DragLayout
import java.util.ArrayList

/**
 * @author zhangliyang
 * @date 2021/5/25
 * GitHub: https://github.com/ZLYang110
 * dec: 模拟选择弹窗
 */
class SimulateDialog(mContext: Context)  {

    private var title: CharSequence? = null

    private  var context: Context
    private var listener:  OnItemSelectedListener? = null
    private lateinit var mAdapter: BaseQuickAdapter<WeatherDec, BaseViewHolder>
    private var datas: MutableList<WeatherDec> = ArrayList()





    init{
        context= mContext
    }
    companion object {
        private var currSelectPos = -1
        fun with(context: Context): SimulateDialog {
            return SimulateDialog(context)
        }
    }
    fun listener(listener: SimulateDialog.OnItemSelectedListener): SimulateDialog {
        this.listener = listener
        return this
    }
    fun show() {
        datas=WeatherUtil.getWeatherTypeList()
        Upper.dialog(context)
            .contentView(R.layout.dialog_simulate)
            .gravity(Gravity.BOTTOM)
            .backgroundDimDefault()
           // .dragDismiss(DragLayout.DragStyle.Bottom)
            .cancelableOnTouchOutside(true)
            .cancelableOnClickKeyBack(false)
            .bindData(Layer.DataBinder { layer ->
                val tvTitle = layer.getView<TextView>(R.id.basic_ui_tv_dialog_list_title)
                if (title == null) {
                    tvTitle.visibility = View.GONE
                } else {
                    tvTitle.text = title
                }
                val rv = layer.getView<RecyclerView>(R.id.basic_ui_rv_dialog_list)
                rv.layoutManager = LinearLayoutManager(rv.context)
                mAdapter = object :
                    BaseQuickAdapter<WeatherDec, BaseViewHolder>(R.layout.basic_ui_rv_item_dialog_list) {
                    override fun convert(helper: BaseViewHolder, item: WeatherDec) {
                        val tvName = helper.getView<TextView>(R.id.basic_ui_tv_dialog_list_name)
                        if (helper.adapterPosition == currSelectPos) {
                            tvName.setTextColor(ResUtils.getColor(tvName.context, R.color.main))
                        } else {
                            tvName.setTextColor( ResUtils.getColor( tvName.context, R.color.text_surface))
                        }
                        if (item.dayType==0)
                            tvName.text = "白天 · " + item.dec
                        if (item.dayType==1)
                            tvName.text = "晚上 · " + item.dec
                    }
                }
                mAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
                    currSelectPos = position
                    adapter.notifyDataSetChanged()
                    if (listener != null) {
                        listener!!.onSelect(datas[currSelectPos], currSelectPos)
                    }
                    layer.dismiss()
                })
                rv.adapter = mAdapter
                mAdapter.setNewData(datas)
            }).onClickToDismiss(Layer.OnClickListener { _, _ ->
                currSelectPos = -1
                if (listener != null) {
                    listener!!.onSelect(null, currSelectPos)
                }
            }, R.id.tv_dialog_list_no)
            .show()
    }


    interface OnItemSelectedListener {
        fun onSelect(data: WeatherDec?, pos: Int)
    }
}