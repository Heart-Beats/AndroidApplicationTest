package com.example.zhanglei.myapplication.widgets.refresh

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.utils.traverseFindFirstChildView
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState

/**
 * @Author  张磊  on  2020/09/23 at 13:46
 * Email: 913305160@qq.com
 */

abstract class LottieRefreshHeader : FrameLayout, RefreshHeader {

    companion object {
        private const val TAG = "LottieRefreshHeader"
    }

    abstract val headerLayout: Int

    protected lateinit var lottieAnimationView: LottieAnimationView

    protected var pullAnimationSource: LottieAnimationSource = LottieAnimationSource()
    protected var refreshAnimationSource: LottieAnimationSource = LottieAnimationSource()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        context.obtainStyledAttributes(attrs, R.styleable.LottieRefreshHeader, defStyleAttr, 0).also {
            val pullLottieAnimationRes = it.getResourceId(R.styleable.LottieRefreshHeader_pull_lottie_animation_res, 0)
            val pullLottieAnimationUrl = it.getString(R.styleable.LottieRefreshHeader_pull_lottie_animation_url)
            if (pullLottieAnimationRes != 0 || pullLottieAnimationUrl != null) {
                setPullAnimation(rawRes = pullLottieAnimationRes, url = pullLottieAnimationUrl)
            }

            val refreshLottieAnimationRes = it.getResourceId(R.styleable.LottieRefreshHeader_refresh_lottie_animation_res, 0)
            val refreshLottieAnimationUrl = it.getString(R.styleable.LottieRefreshHeader_pull_lottie_animation_url)
            if (refreshLottieAnimationRes != 0 || refreshLottieAnimationUrl != null) {
                setRefreshAnimation(rawRes = refreshLottieAnimationRes, url = refreshLottieAnimationUrl)
            }

            it.getDrawable(R.styleable.LottieRefreshHeader_primary_background)?.run {
                this@LottieRefreshHeader.background = this
            }
        }.recycle()
    }

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
        Log.d(TAG, "onInitialized: 头部高度==$height, 最大下拉高度==$maxDragHeight")
        //抽象类中的this不可在构造方法中使用，存在风险
        val headerView = View.inflate(context, headerLayout, this)

        lottieAnimationView = headerView.traverseFindFirstChildView(LottieAnimationView::class.java)
                ?: throw Exception("布局文件中没有LottieAnimationView")

        //下拉动画需在下拉回调之前设置
        this.lottieAnimationView.setAnimation(pullAnimationSource)
    }

    /**
     * SmartRefreshLayout 调用 setPrimaryColors 或 setPrimaryColorsId 后会回调， 在其中可以设置刷新头的背景
     */
    override fun setPrimaryColors(vararg colors: Int) {
        Log.d(TAG, "setPrimaryColors: ")
        this.setBackgroundColor(colors.getOrNull(0) ?: Color.TRANSPARENT)
    }

    /**
     * 下拉和释放时布局移动回调 , 在 percent 达到 1.0 时 ，松手即会触发 onReleased --> onStartAnimator --> onFinish
     * 		percent = offset / (height * headerTriggerRate）， headerTriggerRate 默认设置 refreshLayout.setHeaderTriggerRate(1)
     *
     * @param isDragging true 手指正在拖动 false 回弹动画
     * @param percent 下拉的百分比值 : percent = offset / (height * headerTriggerRate）
     * @param offset 下拉的像素偏移量  0 - offset - (footerHeight+maxDragHeight)
     * @param height 高度 HeaderHeight or FooterHeight (offset 可以超过 height 此时 percent 大于 1)
     * @param maxDragHeight 最大拖动高度 offset 可以超过 height 参数 但是不会超过 maxDragHeight
     */
    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        Log.d(TAG, "onMoving: 正在下拉中==$isDragging, 百分比 == $percent, 偏移 == $offset, 头部高度 == $height, 最大拖动高度 ==$maxDragHeight")
        if (isDragging) {
            //isDragging 表示下拉
            val animationProgress = if (!pullAnimationSource.isCanUse()) {
                //下拉动画不可用时
                offset.run {
                    if (this <= height) {
                        //下拉未超过头布局时
                        this / height.toFloat()
                    } else 1f
                }
            } else {
                offset / maxDragHeight.toFloat()
            }

            Log.d(TAG, "onMoving: 当前动画进度 == $animationProgress")
            lottieAnimationView.progress = animationProgress
        }
    }

    /**
     * 松手释放时回调
     */
    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        Log.d(TAG, "onReleased: ")
        lottieAnimationView.pauseAnimation()
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
            lottieAnimationView.resumeAnimation()
        } else {
            lottieAnimationView.setAnimation(refreshAnimationSource)
            Log.d(TAG, "onStartAnimator: 执行刷新动画")
            lottieAnimationView.playAnimation()
            //刷新动画重复执行
            lottieAnimationView.repeatCount = LottieDrawable.INFINITE
        }
    }

    /**
     * onStartAnimator --> onFinish 之间的刷新时间：
     *          开始刷新到调用 finishRefresh() 和 finishLoadMore()之间的时间 + 结束刷新中设置的延迟时间
     *
     * 自定义刷新动画结束时回调,之后头部会慢慢收起 ------> onStartAnimator() 方法中需设置动画
     * @param refreshLayout RefreshLayout
     * @param success 数据是否成功刷新或加载
     * @return 关闭刷新，执行回弹动画的延迟时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        Log.d(TAG, "onFinish: 结束刷新动画")
        if (lottieAnimationView.isAnimating) {
            lottieAnimationView.cancelAnimation()
        }
        if (pullAnimationSource.isCanUse()) {
            //刷新动画结束后重置下拉动画
            lottieAnimationView.setAnimation(pullAnimationSource)
        }
        return 0
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    /**
     * 状态改变事件 {@link RefreshState}
     * @param refreshLayout RefreshLayout
     * @param oldState 改变之前的状态
     * @param newState 改变之后的状态
     */
    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {

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
}