package com.jkj.huilaidian.merchant.widget.refresh

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.widgets.refresh.LottieRefreshHeader
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 * @Author  张磊  on  2021/01/12 at 11:44
 * Email: 913305160@qq.com
 */
class CommonRefreshHeader @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LottieRefreshHeader(context, attrs, defStyleAttr) {

    var initBottomOffset = 0

    override val headerLayout: Int
        get() = R.layout.layout_lottie_refresh_header

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {
        //isDragging 表示下拉
        if (isDragging) {

            //下拉至 lottieAnimationView 顶部的距离
            val offsetBottom = height + initBottomOffset - lottieAnimationView.top

            //lottieAnimationView 的顶部显示后还可以拖拽的距离(需要减去底部初始偏移的距离)
            val maxCalculateOffset = lottieAnimationView.top - initBottomOffset

            val newOffset = offset - offsetBottom
            val animationProgress = if (!pullAnimationSource.isCanUse()) {
                //下拉动画不可用时
                offset.run {
                    if (this <= height) {
                        //下拉未超过头布局时
                        this / height.toFloat()
                    } else 1f
                }
            } else {
                //下拉动画可用
                if (newOffset > 0) {
                    // 下拉超过 lottieAnimationView 顶部时
                    newOffset / maxCalculateOffset.toFloat()
                } else 0f
            }

            lottieAnimationView.progress = animationProgress
        }
    }
}