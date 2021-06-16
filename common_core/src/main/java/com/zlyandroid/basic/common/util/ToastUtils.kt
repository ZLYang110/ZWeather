package com.zlyandroid.basic.common.util

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.zlyandroid.basic.common.R
import com.zlylib.upperdialog.Upper
import com.zlylib.upperdialog.common.AnimatorHelper
import com.zlylib.upperdialog.manager.Layer.AnimatorCreator
import java.util.*

/**
 * @author zhangliyang
 * @date 2020/11/26
 * GitHub: https://github.com/ZLYang110
 * desc: 提示
 */

object ToastUtils {

    private val mRandom = Random()

    /**
     * 提示
     * Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL :
     * Gravity.TOP | Gravity.CENTER_HORIZONTAL :
     * Gravity.LEFT | Gravity.CENTER_VERTICAL :
     * Gravity.RIGHT | Gravity.CENTER_VERTICAL
     */
    fun show(context: Context?, msg: String?) {
        show(context, msg, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
    }

    fun show(context: Context?, msg: String?, gravity: Int) {
        Upper.toast(context)
            .duration(3000)
            .message(msg)
            .backgroundColorRes(R.color.third)
            .gravity(gravity)
            .marginBottom(210)
            .animator(object : AnimatorCreator {
                override fun createInAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaInAnim(target)
                }

                override fun createOutAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaOutAnim(target)
                }
            })
            .show()
    }

    /**
     * 正确提示
     *
     * Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL :
     * Gravity.TOP | Gravity.CENTER_HORIZONTAL :
     * Gravity.LEFT | Gravity.CENTER_VERTICAL :
     * Gravity.RIGHT | Gravity.CENTER_VERTICAL
     */
    fun showSuccess(context: Context?, msg: String?) {
        showSuccess(context, msg, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
    }

    fun showSuccess(context: Context?, msg: String?, gravity: Int) {
        Upper.toast(context)
            .duration(3000)
            .icon(R.drawable.ic_success)
            .message(msg)
            .backgroundColorRes(R.color.assist)
            .gravity(gravity)
            .animator(object : AnimatorCreator {
                override fun createInAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaInAnim(target)
                }

                override fun createOutAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaOutAnim(target)
                }
            })
            .show()
    }


    /**
     * 错误提示
     *
     * Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL :
     * Gravity.TOP | Gravity.CENTER_HORIZONTAL :
     * Gravity.LEFT | Gravity.CENTER_VERTICAL :
     * Gravity.RIGHT | Gravity.CENTER_VERTICAL
     */
    fun showFail(context: Context?, msg: String?) {
        showFail(context, msg, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
    }

    fun showFail(context: Context?, msg: String?, gravity: Int) {
        Upper.toast(context)
            .duration(3000)
            .icon(R.drawable.ic_fail)
            .message(msg)
            .backgroundColorRes(R.color.rednrd)
            .gravity(gravity)
            .animator(object : AnimatorCreator {
                override fun createInAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaInAnim(target)
                }

                override fun createOutAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaOutAnim(target)
                }
            })
            .show()
    }

    /**
     * 可自定义
     */
    fun showNormal(context: Context?, msg: String?) {
        val isSucc = mRandom.nextBoolean()
        Upper.toast()
            .duration(3000)
            .icon(if (isSucc) R.drawable.ic_success else R.drawable.ic_fail)
            .message(if (isSucc) " 成功了" else " 失败了")
            .alpha(mRandom.nextFloat())
            .backgroundColorInt(
                Color.argb(
                    mRandom.nextInt(255),
                    mRandom.nextInt(255),
                    mRandom.nextInt(255),
                    mRandom.nextInt(255)
                )
            )
            .gravity(
                if (mRandom.nextBoolean()) if (mRandom.nextBoolean()) Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL else Gravity.TOP or Gravity.CENTER_HORIZONTAL else if (mRandom.nextBoolean()) Gravity.LEFT or Gravity.CENTER_VERTICAL else Gravity.RIGHT or Gravity.CENTER_VERTICAL
            )
            .animator(object : AnimatorCreator {
                override fun createInAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaInAnim(target)
                }

                override fun createOutAnimator(target: View): Animator {
                    return AnimatorHelper.createZoomAlphaOutAnim(target)
                }
            })
            .show()
    }

    private var mToast: Toast? = null //全局唯一的Toast

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    fun showShort(context: Context?, message: CharSequence?) {
        mToast = if (mToast == null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT)
            //mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast!!.cancel()
            Toast.makeText(context, message, Toast.LENGTH_SHORT)
            //mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        this.mToast?.show()
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param resId 资源ID:getResources().getString(R.string.xxxxxx);
     */
    fun showShort(context: Context?, resId: Int) {
        mToast = if (mToast == null) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT)
            // mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            // mToast.setText(resId);
            mToast!!.cancel()
            Toast.makeText(context, resId, Toast.LENGTH_SHORT)
            // mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        this.mToast?.show()
    }
}