package com.zlyandroid.weather.ui.dialog

import android.animation.Animator
import android.content.Context
import android.view.View
import com.zlyandroid.weather.R
import com.zlylib.upperdialog.Upper
import com.zlylib.upperdialog.common.Align
import com.zlylib.upperdialog.common.AnimatorHelper
import com.zlylib.upperdialog.manager.Layer
import com.zlylib.upperdialog.popup.PopupLayer

class MenuPopup(mContext: Context) {
    private var context: Context
    var view: View? = null
    private var listener: OnItemSelectedListener? = null
      var  popup: PopupLayer? = null
        init {
        context = mContext
    }
    companion object {
        fun with(context: Context): MenuPopup {
            return MenuPopup(context)
        }
    }
    fun view(view: View): MenuPopup {
        this.view = view
        return this
    }
    fun listener(listener: OnItemSelectedListener): MenuPopup {
        this.listener = listener
        return this
    }
    fun show() {
       if(popup!=null && popup!!.isShow){
           popup!!.dismiss()
           return
       }
         popup = Upper.popup(view)
          .align(Align.Direction.VERTICAL, Align.Horizontal.ALIGN_RIGHT, Align.Vertical.BELOW, true)
          .offsetYdp(1f)
          .offsetXdp(-10f)
          .outsideTouchedToDismiss(true)
          .outsideInterceptTouchEvent(false)
          .contentView(R.layout.popup_meun) as PopupLayer
        popup!!.contentAnimator(object :  Layer.AnimatorCreator{
              override fun createInAnimator(target: View?): Animator {
                  return AnimatorHelper.createTopInAnim(target)
              }
              override fun createOutAnimator(target: View?): Animator {
                  return AnimatorHelper.createTopOutAnim(target)
              }
          }).onClickToDismiss(object :Layer.OnClickListener{
              override fun onClick(layer: Layer, v: View) {
                  listener?.onSelect(0)
              }
          },R.id.tv_dialog_menu_1)
          .onClickToDismiss(object :Layer.OnClickListener{
              override fun onClick(layer: Layer, v: View) {
                  listener?.onSelect(1)
              }
          },R.id.tv_dialog_menu_2)
          .onClickToDismiss(object :Layer.OnClickListener{
              override fun onClick(layer: Layer, v: View) {
                  listener?.onSelect(2)
              }
          },R.id.tv_dialog_menu_3).onDismissListener(object :Layer.OnDismissListener{
                override fun onDismissed(layer: Layer?) {
                    popup=null
                }
                override fun onDismissing(layer: Layer?) {
                }

            })

     .show()
    }


    interface OnItemSelectedListener {
        fun onSelect(pos: Int)
    }
}