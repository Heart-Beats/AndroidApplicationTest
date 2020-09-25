package com.example.zhanglei.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.util.traverseFindFirstChildView
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout

/**
 * @Author  张磊  on  2020/09/23 at 13:46
 * Email: 913305160@qq.com
 */

abstract class LottieRefreshHeader : FrameLayout, RefreshHeader {

	private val TAG = "LottieRefreshHeader"

	abstract val headerLayout: Int

	private var lottieAnimationView: LottieAnimationView? = null

	private var pullAnimationSource: LottieAnimationSource = LottieAnimationSource()
	private var refreshAnimationSource: LottieAnimationSource = LottieAnimationSource()

	constructor(context: Context) : this(context, null)
	constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, R.style.LottieRefreshHeaderStyle)
	constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

	override fun getView(): View {
		return this
	}

	/**
	 * 尺寸定义完成 ---> setXXXHeader()之后就会调用
	 * 如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
	 * @param kernel RefreshKernel
	 * @param height HeaderHeight or FooterHeight
	 * @param maxDragHeight 最大拖动高度
	 */
	override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onInitialized: ")
		//抽象类中的this不可在构造方法中使用，存在风险
		val headerView = View.inflate(context, headerLayout, this)

		lottieAnimationView = headerView.traverseFindFirstChildView(LottieAnimationView::class.java)

		if (lottieAnimationView == null) {
			throw Exception("布局文件中没有LottieAnimationView")
		}

		//下拉动画需在下拉回调之前设置
		this.lottieAnimationView?.setAnimation(pullAnimationSource)
	}

	override fun setPrimaryColors(vararg colors: Int) {
	}

	//下拉和释放时布局移动回调
	override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onMoving: 百分比 == $percent, 偏移 == $offset, 头部高度 == $height, 最大拖动高度 == $maxDragHeight")
		if (isDragging) {
			//isDragging 表示下拉
			if (!pullAnimationSource.isCanUse()) {
				lottieAnimationView?.progress = offset.toFloat() / maxDragHeight / 2
			} else {
				lottieAnimationView?.progress = percent
			}
		}
	}

	//松手释放时回调
	override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onReleased: ")
		lottieAnimationView?.pauseAnimation()
	}

	/**
	 * 下拉释放回到头部高度时回调  -----> 可在此添加刷新时的自定义动画
	 * @param refreshLayout RefreshLayout
	 * @param height 高度 HeaderHeight or FooterHeight
	 * @param maxDragHeight 最大拖动高度
	 */
	override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
		Log.d(TAG, "onStartAnimator: 开始动画")
		if (!refreshAnimationSource.isCanUse()) {
			lottieAnimationView?.resumeAnimation()
		} else {
			lottieAnimationView?.setAnimation(refreshAnimationSource)
			lottieAnimationView?.playAnimation()
		}
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
		if (pullAnimationSource.isCanUse()) {
			lottieAnimationView?.setAnimation(pullAnimationSource)
		}
		return 0
	}

	override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
	}

	override fun isSupportHorizontalDrag(): Boolean {
		return false
	}

	fun setPullAnimation(rawRes: Int? = null, url: String? = null) {
		this.pullAnimationSource = LottieAnimationSource(rawRes, url)
	}

	fun setRefreshAnimation(rawRes: Int? = null, url: String? = null) {
		this.refreshAnimationSource = LottieAnimationSource(rawRes, url)
	}

	private fun LottieAnimationView.setAnimation(lottieAnimationSource: LottieAnimationSource) {
		lottieAnimationSource.rawRes?.also {
			this.setAnimation(it)
		}
		lottieAnimationSource.url?.also {
			this.setAnimationFromUrl(it)
		}
	}

	private data class LottieAnimationSource(
			val rawRes: Int? = null,
			val url: String? = null
	) {
		fun isCanUse(): Boolean {
			return rawRes != null || url != null
		}
	}
}