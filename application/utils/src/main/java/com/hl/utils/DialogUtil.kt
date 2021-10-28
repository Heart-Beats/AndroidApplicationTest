package com.hl.utils

import android.view.View
import android.view.Window
import androidx.appcompat.app.AlertDialog

/**
 * @author  张磊  on  2021/10/28 at 20:04
 * Email: 913305160@qq.com
 */
object DialogUtil {

	/**
	 * 该方法主要给 AlertDialog 自定义填充 ConstraintLayout 使用 layoutId 时用反射获取自定义的 view
	 *
	 * @param  alertDialog 创建出来的 alertDialog
	 * @return 填充的自定义 view 根布局
	 */
	fun getAlertDialogCustomView(alertDialog: AlertDialog?): View? {
		var view: View? = null
		try {
			val alertDialogClass = AlertDialog::class.java
			val mAlert = alertDialogClass.getDeclaredField("mAlert")
			mAlert.isAccessible = true
			val alertController = mAlert[alertDialog]
			mAlert.isAccessible = false
			val alertControllerClass: Class<*> = alertController.javaClass
			val mWindowFiled = alertControllerClass.getDeclaredField("mWindow")
			mWindowFiled.isAccessible = true
			val window = mWindowFiled[alertController] as Window
			mWindowFiled.isAccessible = false
			view = window.findViewById(R.id.custom)
		} catch (e: NoSuchFieldException) {
			e.printStackTrace()
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
		}
		return view
	}
}