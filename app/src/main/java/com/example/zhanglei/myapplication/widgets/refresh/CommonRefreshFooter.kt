package com.jkj.huilaidian.merchant.widget.refresh

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.widgets.refresh.LottieRefreshHeaderFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import kotlinx.android.synthetic.main.layout_common_refresh_footer.view.*

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

    override val headerOrFooterLayout: Int
        get() = R.layout.layout_common_refresh_footer

    override val hasLottieAnimationView: Boolean
        get() = false

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {
        View.inflate(context, headerOrFooterLayout, this)
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        Log.d(TAG, "onFinish: 加载是否成功 == $success")
        state_tips.text = if (success) "加载成功" else "加载失败"
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
                motion_layout.setTransition(R.id.pull_up_to_load_animate)
                motion_layout.transitionToState(R.id.pull_up_to_load)
            }

            RefreshState.LoadReleased -> {
                // 执行回弹动画不作任何操作
            }

            RefreshState.Loading -> {
                pull_to_refresh.setImageResource(R.drawable.loading)
                state_tips.text = "正在加载"
                stopAnimation = false

                val valueAnimator = ValueAnimator.ofFloat(0f, 360f)
                valueAnimator.setDuration(500).interpolator = LinearInterpolator()
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.addUpdateListener {
                    pull_to_refresh.rotation = it.animatedValue as Float
                    if (stopAnimation) {
                        valueAnimator.cancel()
                    }
                }
                valueAnimator.start()
            }

            RefreshState.LoadFinish -> {
                stopAnimation = true
                pull_to_refresh.visibility = View.GONE
            }

            else -> {
            }
        }
    }

    private fun initFooterLayout() {
        if (!noMoreData) {
            pull_to_refresh.setImageResource(R.drawable.ic_upward_pull_24)
            pull_to_refresh.visibility = VISIBLE
            motion_layout.visibility = VISIBLE
            motion_layout.setTransition(R.id.pull_up_to_load_animate)
            motion_layout.transitionToState(R.id.start)
        } else {
            motion_layout.transitionToState(R.id.no_more_data)
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