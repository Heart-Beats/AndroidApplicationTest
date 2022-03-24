package com.hl.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideSoftInputFromWindow() {
    val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    manager?.hideSoftInputFromWindow(windowToken, 0)
}