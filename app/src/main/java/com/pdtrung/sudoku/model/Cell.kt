package com.pdtrung.sudoku.model

import android.animation.ArgbEvaluator
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.util.Property
import android.view.animation.LinearInterpolator


data class Cell(
    val row: Int,
    val col: Int,
    var value: Int,
    var solvedValue: Int,
    var isStartingCell: Boolean = false
) {
    var notes = mutableSetOf<Int>()

    var isSolved = solvedValue == value

    private var _isAnimation: Boolean = false

    fun setAnimation(b: Boolean) {
        this._isAnimation = b
    }

    fun getAnimation(): Boolean {
        if (!_isAnimation)
            return _isAnimation

        _isAnimation = false
        return true

    }

    var whatIsThat: Int = -1
    var q = 16777215
    var r = 16777215


    var s: Property<Cell, Int>? = null
    var t: Property<Cell, Int>? = null

    class A(cls: Class<Int>, str: String) : Property<Cell, Int>(cls, str) {
        override fun get(`object`: Cell?): Int {
            return Integer.valueOf(`object`!!.whatIsThat)
        }

        override fun set(`object`: Cell?, value: Int?) {
            `object`!!.whatIsThat = value!!
        }
    }

    class B(cls: Class<Int>, str: String) : Property<Cell, Int>(cls, str) {
        override fun get(`object`: Cell?): Int {
            return Integer.valueOf(`object`!!.q)
        }

        override fun set(`object`: Cell?, value: Int?) {
            `object`!!.q = value!!
        }
    }

    class C(cls: Class<Int>, str: String) : Property<Cell, Int>(cls, str) {
        override fun get(`object`: Cell?): Int {
            return Integer.valueOf(`object`!!.r)
        }

        override fun set(`object`: Cell?, value: Int?) {
            `object`!!.r = value!!
        }
    }

    init {
        val cls = Int::class.java
        A(cls, "colorTint")
        s = B(cls, "colorPreOverlay")
        t = C(cls, "colorPostOverlay")
    }

    fun d(
        i2: Int,
        animatorUpdateListener: AnimatorUpdateListener,
        i3: Int,
        i4: Int,
        i5: Int
    ): ObjectAnimator? {
        //F(i3)
        val ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofKeyframe(
                t,
                Keyframe.ofInt(0.0f, i3), Keyframe.ofInt(0.25f, i4), Keyframe.ofInt(1.0f, i5)
            )
        )
        ofPropertyValuesHolder.setEvaluator(ArgbEvaluator())
        ofPropertyValuesHolder.interpolator = LinearInterpolator()
        ofPropertyValuesHolder.startDelay = (i2 * 70).toLong()
        ofPropertyValuesHolder.duration = 500
        ofPropertyValuesHolder.addUpdateListener(animatorUpdateListener)
        return ofPropertyValuesHolder
    }

    fun e(
        i2: Int,
        animatorUpdateListener: AnimatorUpdateListener?,
        i3: Int,
        i4: Int
    ): ObjectAnimator {
        //G(i3)
        val ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(
            this,
            PropertyValuesHolder.ofKeyframe(
                s,
                Keyframe.ofInt(0.0f, i3),
                Keyframe.ofInt(0.25f, i4),
                Keyframe.ofInt(1.0f, i3)
            )
        )
        ofPropertyValuesHolder.setEvaluator(ArgbEvaluator())
        ofPropertyValuesHolder.interpolator = LinearInterpolator()
        ofPropertyValuesHolder.startDelay = (i2 * 40).toLong()
        ofPropertyValuesHolder.duration = 500
        ofPropertyValuesHolder.addUpdateListener(animatorUpdateListener)
        return ofPropertyValuesHolder
    }

}