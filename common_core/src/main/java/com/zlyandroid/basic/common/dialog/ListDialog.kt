package com.zlyandroid.basic.common.dialog

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zlyandroid.basic.common.R
import com.zlylib.upperdialog.Upper
import com.zlylib.upperdialog.manager.Layer
import com.zlylib.upperdialog.manager.Layer.DataBinder
import com.zlylib.upperdialog.utils.ResUtils
import com.zlylib.upperdialog.view.DragLayout
import java.util.*
/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 */
class  ListDialog(mContext: Context) {

    private  var context: Context
    private var title: CharSequence? = null
    private var yesText: CharSequence? = null
    private var noText: CharSequence? = null
    private var noBtn = false

    //是否显示确定
    private var noYesBtn = false

    //是否显示取消
    private var noNoBtn = false
    private var cancelable = true
    private var listener: OnItemSelectedListener? = null
    private lateinit var mAdapter: BaseQuickAdapter<String, BaseViewHolder>
    private var datas: MutableList<String> = ArrayList()
    private var currSelectPos = -1

    init{
        context= mContext
    }
    companion object {
        fun with(context: Context): ListDialog {
            return ListDialog(context)
        }
    }

    fun title(title: CharSequence?): ListDialog {
        this.title = title
        return this
    }

    fun title(@StringRes title: Int): ListDialog {
        this.title = context.getString(title)
        return this
    }

    fun yesText(yesText: CharSequence?): ListDialog {
        this.yesText = yesText
        return this
    }

    fun yesText(@StringRes yesText: Int): ListDialog {
        this.yesText = context.getString(yesText)
        return this
    }

    fun noText(noText: CharSequence?): ListDialog {
        this.noText = noText
        return this
    }

    fun noText(@StringRes noText: Int): ListDialog {
        this.noText = context.getString(noText)
        return this
    }

    fun noBtn(): ListDialog {
        noBtn = true
        return this
    }

    fun noYseBtn(): ListDialog {
        noYesBtn = true
        return this
    }

    fun noNoBtn(): ListDialog {
        noNoBtn = true
        return this
    }

    fun cancelable(cancelable: Boolean): ListDialog {
        this.cancelable = cancelable
        return this
    }

    fun datas(datas: List<String>): ListDialog {
        this.datas= ArrayList()
        this.datas.addAll(datas)
        return this
    }

    fun datas(vararg datas: String): ListDialog {
        return datas(Arrays.asList(*datas))
    }


    fun currSelectPos(currSelectPos: Int): ListDialog {
        this.currSelectPos = currSelectPos
        return this
    }

    fun listener(listener: OnItemSelectedListener): ListDialog {
        this.listener = listener
        return this
    }

    fun show() {
        Upper.dialog(context)
            .contentView(R.layout.basic_ui_dialog_list)
            .gravity(Gravity.BOTTOM)
            .backgroundDimDefault()
            .dragDismiss(DragLayout.DragStyle.Bottom)
            .cancelableOnTouchOutside(cancelable)
            .cancelableOnClickKeyBack(cancelable)
            .bindData(DataBinder { layer ->
                val llYesNo = layer.getView<LinearLayout>(R.id.basic_ui_ll_dialog_list_yes_no)
                val vLineH = layer.getView<View>(R.id.basic_ui_v_dialog_list_line_h)
                Log.i("ListDialog","--"+noBtn + "noNoBtn"+noNoBtn)
                if (noBtn) {

                    vLineH.visibility = View.GONE
                    llYesNo.visibility = View.GONE
                } else {
                    vLineH.visibility = View.VISIBLE
                    llYesNo.visibility = View.VISIBLE
                    val tvYes = layer.getView<TextView>(R.id.basic_ui_tv_dialog_list_yes)
                    val tvNo = layer.getView<TextView>(R.id.basic_ui_tv_dialog_list_no)
                    val vLine = layer.getView<View>(R.id.basic_ui_v_dialog_list_line)
                    if (noNoBtn) {
                        tvNo.visibility  = View.GONE
                        vLine.visibility = View.GONE
                    } else if (noYesBtn) {
                        noBtn = true
                        tvYes.visibility = View.GONE
                        vLine.visibility = View.GONE
                    } else {
                        tvYes.visibility = View.VISIBLE
                         tvNo.visibility = View.VISIBLE
                        vLine.visibility = View.VISIBLE
                    }
                    if (yesText != null) {
                        tvYes.text = yesText
                    } else {
                        tvYes.setText(R.string.basic_ui_dialog_btn_yes)
                    }
                    if (noText != null) {
                        tvNo.text = noText
                    } else {
                        tvNo.setText(R.string.basic_ui_dialog_btn_no)
                    }
                }
                val tvTitle = layer.getView<TextView>(R.id.basic_ui_tv_dialog_list_title)
                if (title == null) {
                    tvTitle.visibility = View.GONE
                } else {
                    tvTitle.text = title
                }
                val rv = layer.getView<RecyclerView>(R.id.basic_ui_rv_dialog_list)
                rv.layoutManager = LinearLayoutManager(rv.context)
                mAdapter = object :
                    BaseQuickAdapter<String, BaseViewHolder>(R.layout.basic_ui_rv_item_dialog_list) {
                    override fun convert(helper: BaseViewHolder, item: String?) {
                        val tvName = helper.getView<TextView>(R.id.basic_ui_tv_dialog_list_name)
                        if (helper.adapterPosition == currSelectPos) {
                            tvName.setTextColor(ResUtils.getColor(tvName.context, R.color.main))
                        } else {
                            tvName.setTextColor( ResUtils.getColor( tvName.context, R.color.text_surface))
                        }
                        tvName.text = item
                    }
                }
                mAdapter.setOnItemClickListener(BaseQuickAdapter.OnItemClickListener { adapter, _, position ->
                    currSelectPos = position
                    adapter.notifyDataSetChanged()
                    if (noBtn) {
                        if (listener != null) {
                            listener!!.onSelect(datas[currSelectPos], currSelectPos)
                        }
                        layer.dismiss()
                    }
                })
                rv.adapter = mAdapter
                mAdapter.setNewData(datas)
            })
            .onClickToDismiss(Layer.OnClickListener { _, _ ->
                if (listener != null) {
                    listener!!.onSelect(datas[currSelectPos], currSelectPos)
                }
            }, R.id.basic_ui_tv_dialog_list_yes)
            .onClickToDismiss(R.id.basic_ui_tv_dialog_list_no)
            .show()
    }


    interface OnItemSelectedListener {
        fun onSelect(data: String, pos: Int)
    }
}