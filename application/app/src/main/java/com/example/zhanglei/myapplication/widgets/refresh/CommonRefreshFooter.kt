package com.example.zhanglei.myapplication.widgets.refresh

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.zhanglei.myapplication.R
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 * @Author  张磊  on  2021/01/18 at 11:23
 * Email: 913305160@qq.com
 */
class CommonRefreshFooter @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LottieRefreshHeaderFooter(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "CommonRefreshFooter"
    }

    private var stopAnimation: Boolean = false
    private var noMoreData: Boolean = false

    private var stateTips: TextView? = null
    private var motionLayout: MotionLayout? = null
    private var pullToRefresh: ImageView? = null

    override val headerOrFooterLayout: Int
        get() = R.layout.layout_common_refresh_footer

    override val hasLottieAnimationView: Boolean
        get() = false

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        View.inflate(context, headerOrFooterLayout, this).run {
            stateTips = this.findViewById(R.id.state_tips)
            motionLayout = this.findViewById(R.id.motion_layout)
            pullToRefresh = this.findViewById(R.id.pull_to_refresh)
        }
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        Log.d(TAG, "onFinish: 加载是否成功 == $success")
        stateTips?.text = if (success) "加载成功" else "加载失败"
        return 0
    }


    /**
     * 完整状态流转： None --->PullUpToLoad  --->PullUpCanceled --->ReleaseToLoad --->LoadReleased --->Loading --->LoadFinish --->None
     *                          上拉中           取消上拉加载       达到松手刷新临界        开始松手       加载刷新      加载结束
     */
    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        super.onStateChanged(refreshLayout, oldState, newState)
        Log.d(TAG, "onStateChanged: $oldState ---> $newState")

        when (newState) {
            RefreshState.PullUpToLoad -> {
                initFooterLayout()
            }

            RefreshState.ReleaseToLoad -> {
                motionLayout?.setTransition(R.id.pull_up_to_load_animate)
                motionLayout?.transitionToState(R.id.pull_up_to_load)
            }

            RefreshState.LoadReleased -> {
                // 执行回弹动画不作任何操作
            }

            RefreshState.Loading -> {
                pullToRefresh?.setImageResource(R.drawable.loading)
                stateTips?.text = "正在加载"
                stopAnimation = false

                val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
                valueAnimator.setDuration(500).interpolator = LinearInterpolator()
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.addUpdateListener {
                    pullToRefresh?.rotation = it.animatedValue as Float
                    if (stopAnimation) {
                        valueAnimator.cancel()
                    }
                }
                valueAnimator.start()
            }

            RefreshState.LoadFinish -> {
                stopAnimation = true
                pullToRefresh?.visibility = View.GONE
            }

            else -> {
            }
        }
    }

    private fun initFooterLayout() {
        if (!noMoreData) {
            pullToRefresh?.setImageResource(R.drawable.ic_upward_pull_24)
            pullToRefresh?.visibility = VISIBLE
            motionLayout?.visibility = VISIBLE
            motionLayout?.setTransition(R.id.pull_up_to_load_animate)
            motionLayout?.transitionToState(R.id.start)
        } else {
            motionLayout?.transitionToState(R.id.no_more_data)
        }
    }

    /**
     * 仅在 noMoreData == true 时才有回调
     */
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        Log.d(TAG, "setNoMoreData: 没有更多数据 == $noMoreData")
        this.noMoreData = noMoreData
        initFooterLayout()
        return super.setNoMoreData(noMoreData)
    }
}