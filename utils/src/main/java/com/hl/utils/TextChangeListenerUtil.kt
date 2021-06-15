package com.hl.utils

import android.text.Editable
import android.text.TextWatcher

/**
 * @author  张磊  on  2021/06/07 at 9:38
 * Email: 913305160@qq.com
 */
class TextChangedListener(
	private val beforeTextChanged: (
		text: CharSequence?,
		start: Int,
		count: Int,
		after: Int
	) -> Unit = { _, _, _, _ -> },
	private val onTextChanged: (
		text: CharSequence?,
		start: Int,
		before: Int,
		count: Int
	) -> Unit = { _, _, _, _ -> },
	private val afterTextChanged: (editable: Editable?, textWatcher: TextWatcher) -> Unit = { _, _ -> }
) : TextWatcher {
	override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {
		beforeTextChanged.invoke(text, start, count, after)
	}

	override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
		onTextChanged.invoke(text, start, before, count)
	}

	override fun afterTextChanged(s: Editable?) {
		afterTextChanged.invoke(s, this)
	}
}