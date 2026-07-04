/*
 * SPDX-FileCopyrightText: 2006 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.calculator2

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.google.android.material.button.MaterialButton

/**
 * A basic Button that vibrates on finger down.
 */
class HapticButton : MaterialButton {
    constructor(context: Context) : super(context) {
        initVibration()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initVibration()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initVibration()
    }

    private fun initVibration() {
        setOnTouchListener { v: View?, event: MotionEvent? ->
            if (event!!.action == MotionEvent.ACTION_DOWN) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
            } else if (event.action == MotionEvent.ACTION_UP) {
                v!!.performClick()
                v.isPressed = false
            }
            false
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
        super.onSizeChanged(w, h, oldW, oldH)
        if (icon == null) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, h * 0.4f)
        }
    }
}
