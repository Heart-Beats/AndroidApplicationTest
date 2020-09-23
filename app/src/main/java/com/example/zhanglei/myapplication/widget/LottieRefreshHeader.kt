package com.example.zhanglei.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.util.traverseFindFirstChildView
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlin.math.log

/**
 * @Author  张磊  on  2020/09/23 at 13:46
 * Email: 913305160@qq.com
 */

abstract class LottieRefreshHeader : FrameLayout, RefreshHeader {

	private val TAG = "LottieRefreshHeader"

	abstract val headerLayout: Int

	private var lottieAnimationView: LottieAnimationView? = null

	constructor(context: Context) : this(context, null)
	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.style.LottieRefreshHeaderStyle)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun getView(): View {
		return this
	}

	override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
		//抽象类中的this不可在构造方法中使用，存在风险
		val headerView = LayoutInflater.from(context).inflate(headerLayout, this)

		lottieAnimationView = headerView.traverseFindFirstChildView(LottieAnimationView::class.java)

		if (lottieAnimationView == null) {
			throw Exception("布局文件中没有LottieAnimationView")
		}
	}

	//下拉和释放时布局移动回调
	override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onMoving: 百分比 == $percent, 偏移 == $offset, 头部高度 == $height, 最大拖动高度 == $maxDragHeight")
		if (isDragging) {
			lottieAnimationView?.progress = percent
			// lottieAnimationView?.progress = offset.toFloat() / maxDragHeight
		}
	}

	//松手释放时回调
	override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onReleased: ")
	}

	/**
	 * 下拉释放回到头部高度时回调  -----> 可在此添加刷新时的自定义动画
	 * @param refreshLayout RefreshLayout
	 * @param height HeaderHeight or FooterHeight
	 * @param maxDragHeight 最大拖动高度
	 */
	override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onStartAnimator: 开始动画")
	}

	/**
	 * 自定义动画结束时回调 ------> onStartAnimator() 方法中需设置动画
	 * @param refreshLayout RefreshLayout
	 * @param success 数据是否成功刷新或加载
	 * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
	 */
	override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
		Log.d(TAG, "onFinish: 结束动画")
		lottieAnimationView?.cancelAnimation()
		return 500
	}

	override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
	}

	override fun isSupportHorizontalDrag(): Boolean {
		return false
	}
}