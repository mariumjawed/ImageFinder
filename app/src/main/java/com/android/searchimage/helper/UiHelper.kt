package com.android.searchimage.helper

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.widget.ProgressBar
import com.kaopiz.kprogresshud.KProgressHUD

/**
 * library dependednt
 */
private var mDialog: KProgressHUD? = null

fun getProgressHUD(context: Context): KProgressHUD? {
    val progressBar = ProgressBar(context, null, R.attr.progressBarStyle)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        progressBar.indeterminateTintList = ColorStateList.valueOf(
            context.getResources().getColor(R.color.white)
        )
    }
    val progressHUD: KProgressHUD = KProgressHUD.create(context)

    progressHUD
        .setCancellable(false)
        .setCustomView(progressBar)
        .setLabel("Loading...", context.getResources().getColor(R.color.white))
    return progressHUD
}

 fun show(activity: Activity) {
    mDialog = getProgressHUD(activity)
    mDialog = getProgressHUD(activity)
    if (!(activity).isFinishing) mDialog?.show()
}

 fun dismissDialog() {
    if (mDialog != null) {
        mDialog!!.dismiss()
    }
}