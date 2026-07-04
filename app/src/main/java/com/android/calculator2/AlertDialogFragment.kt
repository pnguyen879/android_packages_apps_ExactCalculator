/*
 * SPDX-FileCopyrightText: 2015 The Android Open Source Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.calculator2

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Display a message with a dismiss button, and optionally a second button.
 */
class AlertDialogFragment : DialogFragment(), DialogInterface.OnClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments ?: Bundle.EMPTY
        val builder = MaterialAlertDialogBuilder(requireActivity())

        val inflater = getLayoutInflater()
        val messageView = inflater.inflate(
            R.layout.dialog_message, null /* root */
        ) as TextView
        messageView.text = args!!.getCharSequence(KEY_MESSAGE)
        builder.setView(messageView)

        builder.setNegativeButton(args.getCharSequence(KEY_BUTTON_NEGATIVE), null /* listener */)

        val positiveButtonLabel = args.getCharSequence(KEY_BUTTON_POSITIVE)
        if (positiveButtonLabel != null) {
            builder.setPositiveButton(positiveButtonLabel, this)
        }

        builder.setTitle(args.getCharSequence(KEY_TITLE))

        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val activity: Activity? = getActivity()
        if (activity is OnClickListener /* always true */) {
            (activity as OnClickListener).onClick(this, which)
        }
    }

    interface OnClickListener {
        /**
         * This method will be invoked when a button in the dialog is clicked.
         * 
         * @param fragment the AlertDialogFragment that received the click
         * @param which    the button that was clicked (e.g.
         * [DialogInterface.BUTTON_POSITIVE]) or the position
         * of the item clicked
         */
        fun onClick(fragment: AlertDialogFragment?, which: Int)
    }

    companion object {
        private val NAME: String = AlertDialogFragment::class.java.name
        private val KEY_MESSAGE: String = NAME + "_message"
        private val KEY_BUTTON_NEGATIVE: String = NAME + "_button_negative"
        private val KEY_BUTTON_POSITIVE: String = NAME + "_button_positive"
        private val KEY_TITLE: String = NAME + "_title"

        /**
         * Convenience method for creating and showing a DialogFragment with the given message and
         * title.
         * 
         * @param activity            originating Activity
         * @param title               resource id for the title string
         * @param message             resource id for the displayed message string
         * @param positiveButtonLabel label for second button, if any.  If non-null, activity must
         * implement AlertDialogFragment.OnClickListener to respond.
         */
        @JvmStatic
        fun showMessageDialog(
            activity: AppCompatActivity, @StringRes title: Int,
            @StringRes message: Int, @StringRes positiveButtonLabel: Int,
            tag: String?
        ) {
            showMessageDialog(
                activity, if (title != 0) activity.getString(title) else null,
                activity.getString(message),
                if (positiveButtonLabel != 0) activity.getString(positiveButtonLabel) else null,
                tag
            )
        }

        /**
         * Create and show a DialogFragment with the given message.
         * 
         * @param activity            originating Activity
         * @param title               displayed title, if any
         * @param message             displayed message
         * @param positiveButtonLabel label for second button, if any.  If non-null, activity must
         * implement AlertDialogFragment.OnClickListener to respond.
         */
        @JvmStatic
        fun showMessageDialog(
            activity: AppCompatActivity, title: CharSequence?,
            message: CharSequence?,
            positiveButtonLabel: CharSequence?,
            tag: String?
        ) {
            val manager = activity.supportFragmentManager
            if (manager.isDestroyed) {
                return
            }
            val dialogFragment = AlertDialogFragment()
            val args = Bundle()
            args.putCharSequence(KEY_MESSAGE, message)
            args.putCharSequence(KEY_BUTTON_NEGATIVE, activity.getString(R.string.dismiss))
            if (positiveButtonLabel != null) {
                args.putCharSequence(KEY_BUTTON_POSITIVE, positiveButtonLabel)
            }
            args.putCharSequence(KEY_TITLE, title)
            dialogFragment.setArguments(args)
            dialogFragment.show(manager, tag /* tag */)
        }
    }
}
