package com.example.zhanglei.myapplication.fragments

import android.Manifest.permission
import android.os.Bundle
import android.view.View
import com.elvishew.xlog.XLog
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.utils.DeviceInfoUtil
import com.example.zhanglei.myapplication.utils.reqPermissions
import com.example.zhanglei.myapplication.widgets.refresh.LottieRefreshHeaderFooter
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : BaseFragment() {

	override val layoutResId: Int
		get() = R.layout.fragment_third


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val lottieRefreshHeader = object : LottieRefreshHeaderFooter(requireContext()) {
			override val headerOrFooterLayout: Int
				get() = R.layout.layout_lottie_refresh_header

			override val hasLottieAnimationView: Boolean
				get() = true

			override fun getSpinnerStyle(): SpinnerStyle {
				return SpinnerStyle.FixedFront
			}
		}
		lottieRefreshHeader.setPullAnimation(url = "https://assets4.lottiefiles.com/private_files/lf30_hp2n68rf.json")
		lottieRefreshHeader.setRefreshAnimation(url = "https://assets3.lottiefiles.com/packages/lf20_R6y0Xw.json")
		smart_refresh_layout.setRefreshHeader(lottieRefreshHeader)
		smart_refresh_layout.setHeaderMaxDragRate(1f) //最大下拉高度与Header标准高度的倍数
		smart_refresh_layout.setHeaderHeight(200f)  //Header标准高度（显示下拉高度>=标准高度 触发刷新), 默认高度：100dp
		// smart_refresh_layout.setReboundDuration(1000) //回弹动画时长（毫秒）默认: 300ms
		// smart_refresh_layout.setHeaderInsetStart(200f) //设置 Header 起始位置偏移量
		smart_refresh_layout.setPrimaryColorsId(R.color.colorAccent)

		/*	下拉距离 与 HeaderHeight 的比率达到此值时将会触发刷新（默认1，即下拉距离等于头部高度触发刷新，但若最大下拉高度等于头部高度，
			实际上是无法拉满的，因此几乎不会触发刷新动画，即 onStartAnimator 事件）*/
		smart_refresh_layout.setHeaderTriggerRate(0.7f)

		reqPermissions(permission.READ_PHONE_STATE, allGrantedAction = {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
				XLog.d(DeviceInfoUtil.getDeviceAllInfo(requireContext()))
			}
		})
	}

}