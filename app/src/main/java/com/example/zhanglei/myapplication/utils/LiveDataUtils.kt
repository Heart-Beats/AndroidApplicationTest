package com.jkj.huilaidian.merchant.utils

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @Author  张磊  on  2020/12/09 at 17:17
 * Email: 913305160@qq.com
 */

fun <T> LiveData<T>.onceObserve(owner: LifecycleOwner, onChanged: (T) -> Unit) {
	if (!this.hasObservers()) {
		this.observe(owner, onChanged)
	}
}

fun <T> MutableLiveData<T>.setSafeValue(obj: T?) {
	if (Looper.myLooper() == Looper.getMainLooper()) {
		this.value = obj
	} else {
		this.postValue(obj)
	}
}