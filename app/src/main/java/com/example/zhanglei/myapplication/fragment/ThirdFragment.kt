package com.example.zhanglei.myapplication.fragment

import android.os.Bundle
import android.view.View
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.widget.LottieRefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : BaseFragment() {

	override val layoutResId: Int
		get() = R.layout.fragment_third


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		smart_refresh_layout.setRefreshHeader(object : LottieRefreshHeader(requireContext()) {
			override val headerLayout: Int
				get() = R.layout.layout_lottie_refresh_header

			override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {

			}

			override fun getSpinnerStyle(): SpinnerStyle {
				return SpinnerStyle.Translate
			}

			override fun setPrimaryColors(vararg colors: Int) {

			}

		})
	}

}