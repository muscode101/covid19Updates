package com.muscode.covid19stats.commons

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.muscode.covid19stats.R

class CustomProgressDialog {
    private lateinit var dialog: Dialog

    fun init(context: Context){
        dialog = Dialog(context)
        val inflate = LayoutInflater.from(context).inflate(R.layout.dialog_progress_bar, null)
        dialog.setContentView(inflate)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
    }

    fun show() {
        dialog.show()
    }

    fun hide() {
        dialog.dismiss()
    }
}