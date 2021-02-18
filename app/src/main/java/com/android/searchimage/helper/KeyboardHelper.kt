package com.android.searchimage.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText


fun hideSoftKeyboard(context: Context?, view: View?) {
    if (context == null || view == null) {
        return
    }
    val imm: InputMethodManager = context
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
}
