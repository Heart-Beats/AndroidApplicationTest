package com.hl.uikit

import android.graphics.Color
import android.util.TypedValue
import android.view.Gravity
import android.view.View

class ToolbarParams {
	var gravity: Int? = Gravity.CENTER
	var navigationIcon: Int? = R.drawable.uikit_icon_navigation_back
	var rightActionIcon: Int? = null
	var rightActionText: CharSequence? = null
	var rightActionTextColor: Int = Color.BLACK
	var rightActionListener: ((view: View) -> Unit)? = null
	var title: CharSequence? = ""
	var titleColor: Int? = null
	var backgroundColor: Int? = null
}

fun UIKitToolbar.init(p: ToolbarParams.() -> Unit) {
	val params = ToolbarParams().apply(p)

	val colorPrimary = TypedValue()
	context.theme.resolveAttribute(R.attr.colorPrimary, colorPrimary, true)
	setBackgroundColor(params.backgroundColor ?: colorPrimary.data)
	params.gravity?.let { gravity ->
		setTitleGravity(gravity)
	}
	val navigationIcon = params.navigationIcon
	if (navigationIcon == null) {
		setNavigationIcon(null)
	} else {
		setNavigationIcon(navigationIcon)
	}
	params.title?.let { title ->
		this.title = title
	}
	params.rightActionIcon?.let { icon ->
		setRightActionIcon(icon) {
			params.rightActionListener?.invoke(it)
		}
	}
	params.rightActionText?.let { text ->
		setRightActionText(text) {
			params.rightActionListener?.invoke(it)
		}
	}
	val textColorPrimary = TypedValue()
	context.theme.resolveAttribute(android.R.attr.textColorPrimary, textColorPrimary, true)
	this.setTitleTextColor(params.titleColor ?: textColorPrimary.data)
	this.setRightActionTextColor(params.rightActionTextColor)
}