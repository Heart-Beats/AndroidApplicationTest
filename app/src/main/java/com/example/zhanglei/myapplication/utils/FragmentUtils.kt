package com.example.zhanglei.myapplication.utils

import androidx.fragment.app.Fragment
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.elvishew.xlog.XLog
import java.util.*

/**
 * @Author  张磊  on  2020/08/19 at 16:20
 * Email: 913305160@qq.com
 */

fun Fragment.getLastPage(): CharSequence {
	val navBackStackEntryDeque = ReflectHelper(NavController::class.java).getFiledValue<Deque<NavBackStackEntry>>(findNavController(),
			"mBackStack")

	val stringBuilder = StringBuilder()
	stringBuilder.append(" -------- 导航栈 ----------- \n")
	navBackStackEntryDeque?.forEachIndexed { index, navBackStackEntry ->
		repeat(index + 1) {
			stringBuilder.append("-")
		}
		stringBuilder.append("> ${navBackStackEntry.destination} \n")
		stringBuilder.append("\n")
	}

	XLog.d(stringBuilder.toString())
	val destinationList = navBackStackEntryDeque?.filter {
		it.destination !is NavGraph
	}?.map {
		it.destination
	}

	return destinationList?.getOrNull(destinationList.size - 2)?.label ?: ""
}

fun Fragment.getCurrentDestination(): NavDestination? {
	return try {
		findNavController().currentDestination
	} catch (e: Exception) {
		null
	}
}
