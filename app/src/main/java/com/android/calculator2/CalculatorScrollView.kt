/*
 * SPDX-FileCopyrightText: 2016 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.calculator2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import kotlin.math.max

class CalculatorScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {
    override fun measureChild(
        child: View, parentWidthMeasureSpec: Int,
        parentHeightMeasureSpec: Int
    ) {
        // Allow child to be as wide as they want.
        var parentWidthMeasureSpec = parentWidthMeasureSpec
        parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(parentWidthMeasureSpec), MeasureSpec.UNSPECIFIED
        )

        val lp = child.layoutParams
        val childWidthMeasureSpec: Int = getChildMeasureSpecCompat(
            parentWidthMeasureSpec,
            0,  /* padding */lp.width
        )
        val childHeightMeasureSpec: Int = getChildMeasureSpecCompat(
            parentHeightMeasureSpec,
            paddingTop + paddingBottom, lp.height
        )

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    override fun measureChildWithMargins(
        child: View, parentWidthMeasureSpec: Int, widthUsed: Int,
        parentHeightMeasureSpec: Int, heightUsed: Int
    ) {
        // Allow child to be as wide as they want.
        var parentWidthMeasureSpec = parentWidthMeasureSpec
        parentWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
            MeasureSpec.getSize(parentWidthMeasureSpec), MeasureSpec.UNSPECIFIED
        )

        val lp = child.layoutParams as MarginLayoutParams
        val childWidthMeasureSpec: Int = getChildMeasureSpecCompat(
            parentWidthMeasureSpec,
            lp.leftMargin + lp.rightMargin, lp.width
        )
        val childHeightMeasureSpec: Int = getChildMeasureSpecCompat(
            parentHeightMeasureSpec,
            paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin, lp.height
        )

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
    }

    companion object {
        private fun getChildMeasureSpecCompat(spec: Int, padding: Int, childDimension: Int): Int {
            if (MeasureSpec.getMode(spec) == MeasureSpec.UNSPECIFIED
                && (childDimension == LayoutParams.MATCH_PARENT || childDimension == LayoutParams.WRAP_CONTENT)
            ) {
                val size = max(0, MeasureSpec.getSize(spec) - padding)
                return MeasureSpec.makeMeasureSpec(size, MeasureSpec.UNSPECIFIED)
            }
            return getChildMeasureSpec(spec, padding, childDimension)
        }
    }
}
