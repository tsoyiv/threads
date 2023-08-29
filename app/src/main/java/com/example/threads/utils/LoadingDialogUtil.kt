package com.example.threads.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.threads.R

class LoadingDialogUtil(private val context: Context) {

    private var loadingDialog: AlertDialog? = null

    fun showLoadingDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog?.show()
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }
}
