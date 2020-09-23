package com.example.zhanglei.myapplication.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.get

/**
 * @Author  张磊  on  2020/09/23 at 18:05
 * Email: 913305160@qq.com
 */

fun <T : View> View.traverseFindFirstChildView(findViewType: Class<T>): T? {
	val parentView = this
	if (parentView is ViewGroup) {
		for (i in 0 until parentView.childCount) {
			val childView = parentView[i]
			return childView.traverseFindFirstChildView(findViewType)
		}
	} else {
		if (findViewType.isInstance(parentView)) {
			@Suppress("UNCHECKED_CAST")
			return parentView as T
		}
	}
	return null
}