package com.example.coloring.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.View.MeasureSpec
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.coloring.R

class GenerateImage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), ValueAnimator.AnimatorUpdateListener {

    private var mValueAnimator = ValueAnimator.ofInt(1, 100)
    private val mPaint = Paint()
    private val dp = resources.displayMetrics.density
    private var mBitmapWhite: Bitmap? = null
    private var mBitmapBlack: Bitmap? = null
    private var mBitmapBlue: Bitmap? = null
    private var mBitmapRed: Bitmap? = null
    private var mBitmapYellow: Bitmap? = null
    private var mBitmapGreen: Bitmap? = null
    private var mBitmapPurple: Bitmap? = null
    private var mAnimatedValue = 100
    private var mIsAnimationStart = false
    private var sizeX = 0
    private var sizeY = 0
    private var customSizeX = 0
    private var customSizeY = 0
    private var sizeBitmap = 0F
    private lateinit var array: Array<Array<Int>>

    init {
        mBitmapWhite = ContextCompat.getDrawable(context, R.drawable.pixel_white)?.toBitmap()
        mBitmapBlack = ContextCompat.getDrawable(context, R.drawable.pixel_black)?.toBitmap()
        mBitmapBlue = ContextCompat.getDrawable(context, R.drawable.pixel_blue)?.toBitmap()
        mBitmapRed = ContextCompat.getDrawable(context, R.drawable.pixel_red)?.toBitmap()
        mBitmapYellow = ContextCompat.getDrawable(context, R.drawable.pixel_yellow)?.toBitmap()
        mBitmapGreen = ContextCompat.getDrawable(context, R.drawable.pixel_green)?.toBitmap()
        mBitmapPurple = ContextCompat.getDrawable(context, R.drawable.pixel_purple)?.toBitmap()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        mAnimatedValue = animation?.animatedValue as Int
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mIsAnimationStart) drawAnimatedPixel(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.EXACTLY),
            heightMeasureSpec
        )
        customSizeX = parentWidth
        customSizeY = parentHeight
    }

    fun startAnimation(array: Array<Array<Int>>, sizeBitmap: Float) {
        this.array = array
        this.sizeBitmap = sizeBitmap
        sizeX = (customSizeX / sizeBitmap).toInt()
        sizeY = (customSizeY / sizeBitmap).toInt()
        Log.d("----------", "startAnimation: x: $sizeX y: $sizeY")
        mValueAnimator.duration = 100
        mValueAnimator.interpolator = AccelerateDecelerateInterpolator()
        mValueAnimator.addUpdateListener(this)
        mIsAnimationStart = true
        mValueAnimator.start()
    }

    //taking by 10dp for each pixel
    private fun drawAnimatedPixel(canvas: Canvas?) {
        for (x in 0 until sizeX) {
            for (y in 0 until sizeY) {
                mPaint.alpha = 255

                when (array[x][y]) {
                    0 -> canvas?.drawBitmap(
                        mBitmapBlack!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                    1 -> canvas?.drawBitmap(
                        mBitmapWhite!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                    2 -> canvas?.drawBitmap(
                        mBitmapBlue!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                    3 -> canvas?.drawBitmap(
                        mBitmapRed!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                    4 -> canvas?.drawBitmap(
                        mBitmapYellow!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                    5 -> canvas?.drawBitmap(
                        mBitmapGreen!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                    6 -> canvas?.drawBitmap(
                        mBitmapPurple!!,
                        x * sizeBitmap,
                        y * sizeBitmap,
                        mPaint
                    )
                }

            }
        }
        if (mAnimatedValue == 100) {
            mIsAnimationStart = false
        }
    }
}