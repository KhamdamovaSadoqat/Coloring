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
    private var mAnimatedValue = 100
    private var mIsAnimationStart = false
    private var sizeX = 0
    private var sizeY = 0
    private var customSizeX = 0
    private var customSizeY = 0
    private var sizeBitmap = 0F
    private lateinit var array: Array<Array<Boolean>>

    init {
        mBitmapWhite = ContextCompat.getDrawable(
            context,
            R.drawable.pixel_white
        )?.toBitmap()
        mBitmapBlack = ContextCompat.getDrawable(
            context,
            R.drawable.pixel_black
        )?.toBitmap()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        mAnimatedValue = animation?.animatedValue as Int
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mIsAnimationStart) drawAnimatedPixel(canvas)
//        else drawCircle(canvas)

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

        Log.d("----------", "onMeasure: w: $parentHeight")
        Log.d("----------", "onMeasure: h: $parentWidth")

    }

    fun startAnimation(array: Array<Array<Boolean>>, sizeBitmap: Float) {
        this.array = array
        this.sizeBitmap = sizeBitmap
        sizeX = (customSizeX / sizeBitmap).toInt()
        sizeY = (customSizeY / sizeBitmap).toInt()
        mValueAnimator.duration = 1000
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
                if (array[x][y]) canvas?.drawBitmap(
                    getBitmap(sizeBitmap.toInt(), mBitmapWhite!!)!!,
                    x * sizeBitmap,
                    y * sizeBitmap,
                    mPaint
                )
                else canvas?.drawBitmap(
                    getBitmap(sizeBitmap.toInt(), mBitmapBlack!!)!!,
                    x * sizeBitmap,
                    y * sizeBitmap,
                    mPaint
                )

            }
        }
        if (mAnimatedValue == 100) {
            mIsAnimationStart = false
        }
    }

    private fun drawPixel(canvas: Canvas?) {
        for (x in 0..sizeX) {
            for (y in 0..sizeY) {
                if (array[x][y]) canvas?.drawBitmap(mBitmapWhite!!, x * 10F, y * 10F, mPaint)
                else canvas?.drawBitmap(mBitmapBlack!!, x * 10F, y * 10F, mPaint)

            }
        }
    }

    private fun getBitmap(size: Int, bitmap: Bitmap): Bitmap? {
        return Bitmap.createScaledBitmap(bitmap, size, size, true)
    }

}