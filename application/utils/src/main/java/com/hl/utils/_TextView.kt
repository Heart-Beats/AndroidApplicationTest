package com.hl.utils

import android.graphics.Color
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun TextView.verifyCodeCountDown(scrop: CoroutineScope) {
	scrop.launch {
		var count = 60
		do {
			count--
			isEnabled = false
			text = "已发送($count)"
			delay(1000)
			if (count == 0) {
				isEnabled = true
				text = "重新获取"
			}
		} while (count > 0)
	}
}

fun TextView.setCompareTextColor(percent: Double) {
	val colorString = when {
		percent.toInt() == 0 -> "#ff333333"
		percent > 0 -> "#ffff3b30"
		else -> "#ff00b578"
	}
	setTextColor(Color.parseColor(colorString))
}