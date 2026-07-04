/*
 * SPDX-FileCopyrightText: 2016 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.calculator2

import android.text.Spannable
import android.text.format.DateUtils

class HistoryItem {
    /**
     * This is true only for the "empty history" view.
     */
    val isEmptyView: Boolean
    var evaluatorIndex: Long = 0
        private set

    /**
     * Date in millis
     */
    var timeInMillis: Long = 0
        private set
    var formula: Spannable? = null
        private set

    constructor(evaluatorIndex: Long, millis: Long, formula: Spannable?) {
        this.evaluatorIndex = evaluatorIndex
        this.timeInMillis = millis
        this.formula = formula
        this.isEmptyView = false
    }

    constructor() {
        this.isEmptyView = true
    }

    val dateString: CharSequence?
        /**
         * @return String in format "n days ago"
         * For n > 7, the date is returned.
         */
        get() = DateUtils.getRelativeTimeSpanString(
            this.timeInMillis,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        )
}
