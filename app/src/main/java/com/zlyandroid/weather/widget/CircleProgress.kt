package com.zlyandroid.weather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.zlyandroid.basic.common.util.log.L
import com.zlyandroid.weather.R
import kotlin.math.abs


/**
 * @author zhangliyang
 * @date 2021/1/23
 * GitHub: https://github.com/ZLYang110
 * desc:圆弧进度条
 */
class CircleProgress(context: Context, attrs: AttributeSet) : View(context, attrs) {

    //绘制背景圆弧
    private lateinit var mBgArcPaint: Paint
    private var mBgArcColor: Int = Color.WHITE
    private var mBgArcWidth: Float = 15.toFloat()

    //绘制圆弧
    private  var mArcPaint: Paint
    private var mArcColor: Int = 0
    private var mArcWidth: Float = mBgArcWidth +3
    private var mStartAngle: Float = 125f
    private var mSweepAngle: Float = 290f
    private lateinit var mRectF: RectF

    //圆心坐标，半径
    private  var mCenterPoint: Point
    private var mRadius: Float = 0.toFloat()

    //动画时间
    var animTime: Long = 0
    //属性动画
    private lateinit var mAnimator: ValueAnimator
    //当前进度
    private var mPercent: Float = 0f

    //绘制z中心值
    private var mValuePaint: TextPaint
    private lateinit var mPrecisionFormat: String
    private var mValueColor: Int = Color.WHITE
    private var mValueSize: Float = 100f
    private var mValueOffset: Float = 0f
    private var mValue: Float = 50f
    var maxValue: Int = 100

    //绘制最大值和最小值
    private lateinit var mMaxAndMinPaint: TextPaint
    private var mMaxAndMinXOffset: Float = 0.55f //最小最大文字显示偏差
    private var mMaxAndMinYOffset: Float = 0.9f //最小最大文字显示偏差
    private var mMaxXLocate: Float = 0.toFloat()
    private var mMaxYLocate: Float = 0.toFloat()
    private var mMinXLocate: Float = 0.toFloat()
    private var mMinYLocate: Float = 0.toFloat()
    private var mMaxAndMinColor: Int = Color.WHITE
    private var mMaxAndMinSize: Float = 45f

    //绘制提示
    private var mHintPaint: TextPaint
    var hint: CharSequence? = null
    private var mHintColor: Int = Color.WHITE
    private var mHintSize: Float = 30f
    private var mHintOffset: Float = 0.0f

    //是否开启抗锯齿
    var isAntiAlias: Boolean = true


    //渐变的角度是360度，如果只显示270度，会缺失一段颜色
    private var mSweepGradient: SweepGradient? = null
    private var mGradientColors = intArrayOf(Color.GREEN, Color.GREEN, Color.YELLOW , Color.YELLOW , Color.YELLOW , Color.GREEN )
    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress)
        mBgArcColor = typedArray.getColor(R.styleable.CircleProgress_bgArcColor, Color.WHITE)
        mBgArcWidth = typedArray.getDimension(R.styleable.CircleProgress_bgArcWidth, 15f)
        mArcWidth = typedArray.getDimension(R.styleable.CircleProgress_arcWidth, 18f)
        mStartAngle = typedArray.getFloat(R.styleable.CircleProgress_startAngle, 125f)
        mSweepAngle = typedArray.getFloat( R.styleable.CircleProgress_sweepAngle, 290f)
        animTime = typedArray.getInt(R.styleable.CircleProgress_animTime, 1000).toLong()
        mValue = typedArray.getFloat( R.styleable.CircleProgress_value, 50f)
        mValueColor = typedArray.getColor(R.styleable.CircleProgress_valueColor, Color.BLACK)
        mValueSize = typedArray.getDimension(R.styleable.CircleProgress_valueSize, 100f )
        hint = typedArray.getString(R.styleable.CircleProgress_hint)
        mHintColor = typedArray.getColor(R.styleable.CircleProgress_hintColor, Color.BLACK)
        mHintSize = typedArray.getDimension( R.styleable.CircleProgress_hintSize, 30f)
        maxValue = typedArray.getInt(R.styleable.CircleProgress_maxValue, 100)
        mMaxAndMinColor = typedArray.getColor(R.styleable.CircleProgress_maxAndMinValueColor, Color.GRAY)
        mMaxAndMinSize = typedArray.getDimension( R.styleable.CircleProgress_maxAndMinValueSize, 45f)
        mMaxAndMinXOffset = typedArray.getFloat(R.styleable.CircleProgress_maxAndMinXOffsetPercent, 0.53f)
        mMaxAndMinYOffset = typedArray.getFloat(R.styleable.CircleProgress_maxAndMinYOffsetPercent, 0.95f)

        val gradientArcColors = typedArray.getColor(R.styleable.CircleProgress_arcColors, 0)
        if (gradientArcColors != 0) {
            mArcColor = gradientArcColors
           // mGradientColors = IntArray(2)
           // mGradientColors[0] = gradientArcColors
            //mGradientColors[1] = gradientArcColors
        }
        typedArray.recycle()
    }
    init {
        initAttrs(attrs)

        mRectF = RectF()
        mCenterPoint = Point()


        mArcPaint = Paint()
        mArcPaint.isAntiAlias = isAntiAlias
        //设置画笔样式，FILL,FILL_OR_STROKE,STROKE
        mArcPaint.style = Paint.Style.STROKE
        //设置画笔粗细
        mArcPaint.strokeWidth = mArcWidth
        mArcPaint.strokeCap = Paint.Cap.ROUND


        mBgArcPaint = Paint()
        mBgArcPaint.isAntiAlias = isAntiAlias
        mBgArcPaint.color = mBgArcColor
        mBgArcPaint.style = Paint.Style.STROKE
        mBgArcPaint.strokeWidth = mBgArcWidth
        mBgArcPaint.strokeCap = Paint.Cap.ROUND

        mValuePaint = TextPaint()
        mValuePaint.isAntiAlias = isAntiAlias
        mValuePaint.textSize = mValueSize
        mValuePaint.color = mValueColor
        // 设置Typeface对象，即字体风格，包括粗体，斜体以及衬线体，非衬线体等
        mValuePaint.typeface = Typeface.DEFAULT_BOLD
        mValuePaint.textAlign = Paint.Align.CENTER

        mMaxAndMinPaint = TextPaint()
        mMaxAndMinPaint.color = mMaxAndMinColor
        mMaxAndMinPaint.isAntiAlias = isAntiAlias
        mMaxAndMinPaint.textSize = mMaxAndMinSize
        mMaxAndMinPaint.textAlign = Paint.Align.CENTER


        mHintPaint = TextPaint()
        //设置抗锯齿
        mHintPaint.isAntiAlias = isAntiAlias
        //绘制文字大小
        mHintPaint.textSize = mHintSize
        //设置画笔颜色
        mHintPaint.color = mHintColor
        //从中间向两边绘制，不用再次计算文字
        mHintPaint.textAlign = Paint.Align.CENTER


        //startAnimation()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //求圆弧和圆弧背景的最大宽度
        val minSize = w.coerceAtLeast(h)
        mRadius = (minSize / 2 - 20).toFloat()
        //获取圆的参数
        mCenterPoint.x = w / 2
        mCenterPoint.y = h / 2

        mRectF.left =  mCenterPoint.x.toFloat() - mRadius + mArcWidth / 2
        mRectF.top = mCenterPoint.y.toFloat() - mRadius + mArcWidth / 2
        mRectF.right = mCenterPoint.x.toFloat() + mRadius - mArcWidth / 2
        mRectF.bottom = mCenterPoint.y.toFloat() + mRadius - mArcWidth / 2

        //中心文字
        mValueOffset = mCenterPoint.y + getBaselineOffsetFromY(mValuePaint) / 2
        if (hint != null) {
            var h=getBaselineOffsetFromY(mValuePaint) + getBaselineOffsetFromY(mHintPaint)
            mHintOffset = mCenterPoint.y - h / 3 * 2
            mValueOffset = mCenterPoint.y + h / 3 * 2
        }

        //起点
        mMinXLocate = mCenterPoint.x - mRadius * mMaxAndMinXOffset
        mMinYLocate = mCenterPoint.y.toFloat() + mRadius* mMaxAndMinYOffset + getBaselineOffsetFromY(mMaxAndMinPaint)

        //终点
        mMaxXLocate = mCenterPoint.x + mRadius * mMaxAndMinXOffset
        mMaxYLocate = mCenterPoint.y.toFloat() + mRadius* mMaxAndMinYOffset + getBaselineOffsetFromY(mMaxAndMinPaint)

        updateArcPaint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawArc(canvas)
        drawText(canvas)
    }

    private fun getBaselineOffsetFromY(paint: Paint): Float {
        return measureTextHeight(paint) / 2
    }
    /**
     * 设置文字
     * */
    private fun drawText(canvas: Canvas) {
        mPrecisionFormat = (maxValue * mPercent).toInt().toString()
        canvas.drawText(mPrecisionFormat,mCenterPoint.x.toFloat(), mValueOffset,mValuePaint)
        canvas.drawText("0", mMinXLocate, mMinYLocate, mMaxAndMinPaint)
        canvas.drawText(maxValue.toString(), mMaxXLocate, mMaxYLocate, mMaxAndMinPaint)

        if (hint != null) {
            canvas.drawText( hint.toString(), mCenterPoint.x.toFloat(), mHintOffset, mHintPaint)
        }
    }
    /**
    * 圆弧背景和进度
    * */
    private fun drawArc(canvas: Canvas) {
        canvas.save()
        val currentAngle = mSweepAngle * mPercent
        canvas.rotate(
            mStartAngle,
            mCenterPoint.x.toFloat(),
            mCenterPoint.y.toFloat()
        )//旋转,以stratAngle 为起点
        canvas.drawArc( mRectF, currentAngle, mSweepAngle- currentAngle, false, mBgArcPaint)
        canvas.drawArc(mRectF, 0f, currentAngle, false, mArcPaint)
        canvas.restore()
    }
    /**
     * 更新圆弧画笔
     */
    private fun updateArcPaint() {
        if(mArcColor!=0){
            mArcPaint.color = mArcColor
        }else{
            mSweepGradient = SweepGradient(
                mCenterPoint.x.toFloat(),
                mCenterPoint.y.toFloat(),
                mGradientColors,
                null
            )
            mArcPaint.shader = mSweepGradient
        }
    }
    fun startAnimation() {
        mAnimator = ValueAnimator.ofFloat(0f, mValue / maxValue.toFloat())
        mAnimator.duration = (2000 + 20 * mValue).toLong()
        mAnimator.addUpdateListener { animation ->
            mPercent = animation.animatedValue as Float
            invalidate()
        }
        mAnimator.start()
    }
    /**
     * 设置数值
     */
    fun setValue(value:Float){
        mValue = value
        startAnimation()
    }
    /**
     * 设置标签
     */
    fun setHint(value:String){
        hint = value
        invalidate()
    }

    /**
     * 测量文字高度
     */
    private fun measureTextHeight(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return abs(fontMetrics.ascent) + fontMetrics.descent
    }

    /**
     * 判断此View用户是否可见
     */
    public fun isCover(): Boolean {
        var cover = false
        val rect = Rect()
        cover = getGlobalVisibleRect(rect)
        if (cover) {
            if (rect.width() >= measuredWidth && rect.height() >= measuredHeight) {
                return !cover
            }
        }
        return true
    }

}