package com.muscode.covid19stats.commons

import android.content.SharedPreferences
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.gone() {
    this.visibility = View.GONE
}

fun View.indefiniteSnackbar(
    message: CharSequence,
    actionText: CharSequence,
    action: (View) -> Unit
) = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .setAction(actionText, action)
    .apply { show() }

fun View.snackBar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_LONG)
    .apply { show() }

inline fun SharedPreferences.apply(modifier: SharedPreferences.Editor.() -> Unit) {
    val editor = this.edit()
    editor.modifier()
    editor.apply()
}



fun View.offlineNoticeSnackbar(
    message: CharSequence,
    btn_continue: ((View) -> Unit),
    btn_retry: ((View) -> Unit)
) = Snackbar
    .make(this, message, Snackbar.LENGTH_INDEFINITE)
    .setAction("retry", btn_continue)
    .setAction("continue", btn_retry)
    .apply { show() }