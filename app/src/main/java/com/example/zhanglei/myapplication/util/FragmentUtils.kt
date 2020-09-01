package com.example.zhanglei.myapplication.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.elvishew.xlog.XLog
import java.util.*

/**
 * @Author  张磊  on  2020/09/01 at 12:28
 * Email: 913305160@qq.com
 */

fun Fragment.getLastPage(): CharSequence {
	val navControllerClass = NavController::class.java
	val declaredField = navControllerClass.getDeclaredField("mBackStack")
	declaredField.isAccessible = true
	val navBackStackEntry = declaredField.get(findNavController()) as Deque<NavBackStackEntry>
	declaredField.isAccessible = false

	val stringBuilder = StringBuilder()
	stringBuilder.append(" -------- 导航栈 ----------- \n")
	navBackStackEntry.forEachIndexed { index, navBackStackEntry ->
		repeat(index + 1) {
			stringBuilder.append("-")
		}
		stringBuilder.append("> ${navBackStackEntry.destination} \n")
		stringBuilder.append("\n")
	}

	XLog.d(stringBuilder.toString())
	val destinationList = navBackStackEntry.filter {
		it.destination !is NavGraph
	}.map {
		it.destination
	}

	return destinationList.getOrNull(destinationList.size - 2)?.label ?: ""
}