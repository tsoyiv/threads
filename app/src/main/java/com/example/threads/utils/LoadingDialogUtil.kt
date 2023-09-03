package com.example.threads.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.example.threads.R

class LoadingDialogUtil(private val context: Context) {

    private var myDialog: Dialog? = null

    fun showLoadingDialog() {
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        myDialog = Dialog(context)
        myDialog?.setContentView(dialogView)

        myDialog?.setCancelable(true)
        myDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog?.show()
    }

    fun dismissLoadingDialog() {
        myDialog?.dismiss()
    }
}