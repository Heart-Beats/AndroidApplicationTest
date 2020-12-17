package com.jkj.huilaidian.merchant.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * @Author  张磊  on  2020/12/09 at 17:17
 * Email: 913305160@qq.com
 */

fun <T> LiveData<T>.onceObserve(owner: LifecycleOwner, onChanged: (T) -> Unit) {
	if (!this.hasObservers()) {
		this.observe(owner, onChanged)
	}
}