/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.calculator2

import android.content.Context
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlin.math.abs

class DisplayMotionLayout @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null,
    i: Int = 0
) : MotionLayout(context, attributeSet, i) {
    private val mTouchSlop: Int
    private var mPointerId: Int
    private var mIsScrolling = false
    private var mPreviousPoint: PointF? = null
    private var mPreviousEvent: MotionEvent? = null
    private var mOutOfBounds = false

    init {
        mPointerId = -1
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val hitRect = Rect()

                findViewById<View>(R.id.display).getHitRect(hitRect)
                if (hitRect.contains(motionEvent.x.toInt(), motionEvent.y.toInt())) {
                    mPreviousPoint = PointF(motionEvent.x, motionEvent.y)
                    mPointerId = motionEvent.getPointerId(0)
                    mIsScrolling = false
                    mOutOfBounds = false
                    saveLastMotion(motionEvent)
                } else {
                    mPointerId = -1
                    mIsScrolling = false
                    mOutOfBounds = true
                    clearLastMotion()
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_POINTER_UP -> {
                mIsScrolling = false
                mOutOfBounds = false
                mPointerId = -1
                clearLastMotion()
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = motionEvent.findPointerIndex(mPointerId)
                if (mPointerId != -1 && pointerIndex != -1 && !mOutOfBounds) {
                    val y = abs(motionEvent.getY(pointerIndex) - mPreviousPoint!!.y)
                    if (y > mTouchSlop) {
                        mIsScrolling = true
                        onTouchEvent(mPreviousEvent)
                    }
                }
            }
        }
        if (super.onInterceptTouchEvent(motionEvent)) {
            return true
        }
        return mIsScrolling and !mOutOfBounds
    }

    private fun saveLastMotion(motionEvent: MotionEvent) {
        if (mPreviousEvent != null) {
            mPreviousEvent!!.recycle()
        }
        mPreviousEvent = MotionEvent.obtain(motionEvent)
    }

    private fun clearLastMotion() {
        if (mPreviousEvent != null) {
            mPreviousEvent!!.recycle()
            mPreviousEvent = null
        }
    }
}
