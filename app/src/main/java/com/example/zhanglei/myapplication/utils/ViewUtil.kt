package com.example.zhanglei.myapplication.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.get

/**
 * @Author  张磊  on  2020/09/23 at 18:05
 * Email: 913305160@qq.com
 */

/**
 * 用于从 ViewGroup 及其所有后代子 View 中查找指定的 View 类型
 * @param  findViewType 需要查找的子 View 类型
 * @return 若找到返回第一个找到的 子 View， 否则返回 null
 */
fun <T : View> View.traverseFindFirstChildView(findViewType: Class<T>): T? {
	val parentView = this
	if (findViewType.isInstance(parentView)) {
		@Suppress("UNCHECKED_CAST")
		return parentView as T
	} else {
		if (parentView is ViewGroup) {
			for (i in 0 until parentView.childCount) {
				val childView = parentView[i]
				val findView = childView.traverseFindFirstChildView(findViewType)
				if (findView != null) {
					return findView
				}
			}
		}
		return null
	}
}