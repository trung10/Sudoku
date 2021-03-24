package com.pdtrung.sudoku.view

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation


class TouchEffectAnimator {
    private val EASE_ANIM_DURATION = 200
    private val RIPPLE_ANIM_DURATION = 300
    private val MAX_RIPPLE_ALPHA = 255

    private var mView: View? = null
    private var mClipRadius = 0
    private var hasRippleEffect = false
    private var animDuration = EASE_ANIM_DURATION

    private var requiredRadius = 0
    private var mDownX = 0f
    private var mDownY = 0f
    private var mRadius = 0f
    private var mCircleAlpha = MAX_RIPPLE_ALPHA
    private var mRectAlpha = 0
    private val mCirclePaint = Paint()
    private val mRectPaint = Paint()
    private val mCirclePath = Path()
    private val mRectPath = Path()
    private var isTouchReleased = false
    private var isAnimatingFadeIn = false

    private val animationListener = object : Animation.AnimationListener{
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            isAnimatingFadeIn = false
            if (isTouchReleased) fadeOutEffect()
        }

        override fun onAnimationStart(animation: Animation?) {
            isAnimatingFadeIn = true
        }

    }

    fun TouchEffectAnimator(mView: View?) {
        this.mView = mView
    }

    fun setHasRippleEffect(hasRippleEffect: Boolean) {
        this.hasRippleEffect = hasRippleEffect
        if (hasRippleEffect) animDuration = RIPPLE_ANIM_DURATION
    }

    fun setAnimDuration(animDuration: Int) {
        this.animDuration = animDuration
    }

    fun setEffectColor(effectColor: Int) {
        mCirclePaint.color = effectColor
        mCirclePaint.alpha = mCircleAlpha
        mRectPaint.color = effectColor
        mRectPaint.alpha = mRectAlpha
    }

    fun setClipRadius(mClipRadius: Int) {
        this.mClipRadius = mClipRadius
    }

    fun onTouchEvent(event: MotionEvent) {
        if (event.actionMasked == MotionEvent.ACTION_CANCEL) {
            isTouchReleased = true
            if (!isAnimatingFadeIn) {
                fadeOutEffect()
            }
        }
        if (event.actionMasked == MotionEvent.ACTION_UP) {
            isTouchReleased = true
            if (!isAnimatingFadeIn) {
                fadeOutEffect()
            }
        } else if (event.actionMasked == MotionEvent.ACTION_DOWN) {

            // gets the bigger value (width or height) to fit the circle
            requiredRadius =
                if (mView!!.width > mView!!.height) mView!!.width else mView!!.height

            // increasing radius for circle to reach from corner to other corner
            requiredRadius = (1.2 * requiredRadius).toInt()
            isTouchReleased = false
            mDownX = event.x
            mDownY = event.y
            mCircleAlpha = MAX_RIPPLE_ALPHA
            mRectAlpha = 0
            val valueGeneratorAnim =
                ValueGeneratorAnim(object : InterpolatedTimeCallback {
                    override fun onTimeUpdate(interpolatedTime: Float) {
                        if (hasRippleEffect) mRadius = requiredRadius * interpolatedTime
                        mRectAlpha = (interpolatedTime * MAX_RIPPLE_ALPHA).toInt()
                        mView!!.invalidate()
                    }
                })
            valueGeneratorAnim.interpolator = DecelerateInterpolator()
            valueGeneratorAnim.duration = animDuration.toLong()
            valueGeneratorAnim.setAnimationListener(animationListener)
            mView!!.startAnimation(valueGeneratorAnim)
        }
    }

    fun onDraw(canvas: Canvas) {
        if (hasRippleEffect) {
            mCirclePath.reset()
            mCirclePaint.alpha = mCircleAlpha
            /*mCirclePath.addRoundRect(
                RectF(0, 0, mView!!.width.toFloat(), mView!!.height.toFloat()),
                mClipRadius.toFloat(), mClipRadius.toFloat(), Path.Direction.CW
            )*/
            canvas.clipPath(mCirclePath)
            canvas.drawCircle(mDownX, mDownY, mRadius, mCirclePaint)
        }
        mRectPath.reset()
        if (hasRippleEffect && mCircleAlpha != 255) mRectAlpha = mCircleAlpha / 2
        mRectPaint.alpha = mRectAlpha
        /*canvas.drawRoundRect(
            RectF(0, 0, mView!!.width.toFloat(), mView!!.height.toFloat()), mClipRadius,
            mClipRadius, mRectPaint
        )*/
    }

    private fun fadeOutEffect() {
        val valueGeneratorAnim =
            ValueGeneratorAnim(object : InterpolatedTimeCallback {
                override fun onTimeUpdate(interpolatedTime: Float) {
                    mCircleAlpha = (MAX_RIPPLE_ALPHA - MAX_RIPPLE_ALPHA * interpolatedTime).toInt()
                    mRectAlpha = mCircleAlpha
                    mView!!.invalidate()
                }
            })
        valueGeneratorAnim.duration = animDuration.toLong()
        mView!!.startAnimation(valueGeneratorAnim)
    }

    class ValueGeneratorAnim(private val interpolatedTimeCallback: InterpolatedTimeCallback): Animation() {

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            this.interpolatedTimeCallback.onTimeUpdate(interpolatedTime)
        }
    }

    interface InterpolatedTimeCallback {
        fun onTimeUpdate(interpolatedTime: Float)
    }
}