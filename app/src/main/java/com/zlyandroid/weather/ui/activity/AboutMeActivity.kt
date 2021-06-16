package com.zlyandroid.weather.ui.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.zlyandroid.basic.common.base.BaseActivity
import com.zlyandroid.basic.common.util.CopyUtils
import com.zlyandroid.weather.R
import com.zlyandroid.weather.core.Data
import kotlinx.android.synthetic.main.activity_about_me.*
import java.util.*

/**
 * @author zhangliyang
 * @date 2021/6/13
 * GitHub: https://github.com/ZLYang110
 * desc:关于我
 */
class AboutMeActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AboutMeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_about_me

    override fun initView() {
        weatherView.setWeatherType(Data.showBg)
        piv_wx_qrcode.setImageResource(R.mipmap.weixin)
        ll_github.setOnClickListener(this)
        ll_csdn.setOnClickListener(this)
        changeVisible(View.INVISIBLE, civ_icon, tv_name, tv_sign)
    }

    override fun initData() {
        val targets: MutableList<View> = ArrayList()
        targets.add(civ_icon)
        tv_name.text = "ZLYang110"
        targets.add(tv_name)
        tv_sign.text = "踏实的做，机会总会有的"
        targets.add(tv_sign)
        tv_github.text = " https://github.com/ZLYang110"
        targets.add(ll_github)
        tv_csdn.text = " https://blog.csdn.net/gvvbn"
        targets.add(ll_csdn)
        civ_icon.post {
            changeViewSize(civ_icon, 0f, 1f, 300)
            doDelayShowAnim(800, 60, *targets.toTypedArray())
        }
    }


    override fun onClick2(v: View) {
        super.onClick2(v)
        when (v.id) {
            R.id.ll_github -> {
                CopyUtils.copyText("https://github.com/ZLYang110")
                showToast("已复制")
            }
            R.id.ll_csdn -> {
                CopyUtils.copyText("https://blog.csdn.net/gvvbn")
                showToast("已复制")
            }
        }
    }


    private fun changeVisible(visible: Int, vararg views: View) {
        for (view in views) {
            view.visibility = visible
        }
    }

    private fun changeViewAlpha(target: View, from: Float, to: Float, dur: Long) {
        val animator = ObjectAnimator.ofFloat(target, "alpha", from, to)
        animator.duration = dur
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun changeViewSize(target: View?, from: Float, to: Float, dur: Long) {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = dur
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener(AnimatorUpdateListener { animator ->
            if (target == null) {
                animator.cancel()
                return@AnimatorUpdateListener
            }
            val f = animator.animatedValue as Float
            target.scaleX = f
            target.scaleY = f
        })
        animator.start()
    }

    private fun doDelayShowAnim(dur: Long, delay: Long, vararg targets: View) {
        for (i in 0 until targets.size) {
            val target = targets[i]
            target.alpha = 0f
            val animatorY = ObjectAnimator.ofFloat(target, "translationY", 100f, 0f)
            val animatorA = ObjectAnimator.ofFloat(target, "alpha", 0f, 1f)
            animatorY.duration = dur
            animatorA.duration = (dur * 0.618f).toLong()
            val animator = AnimatorSet()
            animator.playTogether(animatorA, animatorY)
            animator.interpolator = DecelerateInterpolator()
            animator.startDelay = delay * i
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    changeVisible(View.VISIBLE, target)
                }

                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animator.start()
        }
    }

}