package com.hl.utils

import android.app.Dialog
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.hl.uikit.actionsheet.ArrayListSheetDialogFragment
import com.hl.uikit.dialog.AlertDialogFragment

/**
 * @author  张磊  on  2021/11/08 at 11:43
 * Email: 913305160@qq.com
 */

fun Fragment.showAgreeDialog(itemClickListener: (dialog: Dialog, position: Int) -> Unit) {
	ArrayListSheetDialogFragment<CharSequence>().apply {
		val serviceAgreement = HtmlCompat.fromHtml("<font color=\"#205BFF\">《慧徕店服务协议》</font>", 0)
		val privacyAgreement = HtmlCompat.fromHtml("<font color=\"#205BFF\">《慧徕店隐私协议》</font>", 0)
		this.data = listOf(serviceAgreement, privacyAgreement)
		this.addNegativeButton()
		this.itemClickListener = { dialog, position ->
			itemClickListener(dialog, position)
		}
	}.show(parentFragmentManager, "")
}

fun Fragment.popConfirmDialog(
	dialogTitle: String? = null, dialogMessage: CharSequence, confirmText: CharSequence = "确认",
	onConfirmListener: () -> Unit = {}
): AlertDialogFragment {
	val dialogFragment = AlertDialogFragment().apply {
		if (dialogTitle != null) {
			setTitle(dialogTitle)
		}
		setMessage(dialogMessage)
		setPositiveButton(confirmText) { _, _ ->
			dismiss()
			onConfirmListener()
		}
	}
	dialogFragment.show(parentFragmentManager, "")
	return dialogFragment
}

fun Fragment.popConfirmAndCancelDialog(
	dialogTitle: String? = null, dialogMessage: CharSequence,
	cancelText: CharSequence = "取消",
	onCancelListener: () -> Unit = {},
	confirmText: CharSequence = "确认",
	onConfirmListener: () -> Unit = {}
): AlertDialogFragment {
	val dialogFragment = AlertDialogFragment().apply {
		if (dialogTitle != null) {
			setTitle(dialogTitle)
		}
		setMessage(dialogMessage)
		setNegativeButton(cancelText) { _, _ ->
			dismiss()
			onCancelListener()
		}
		setPositiveButton(confirmText) { _, _ ->
			dismiss()
			onConfirmListener()
		}
	}
	dialogFragment.show(parentFragmentManager, "")
	return dialogFragment
}