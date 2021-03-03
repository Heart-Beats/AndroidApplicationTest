package com.example.zhanglei.myapplication.activities.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * @Author  张磊  on  2021/03/02 at 15:36
 * Email: 913305160@qq.com
 */
abstract class ViewBindingBaseActivity<T : ViewBinding> : BaseActivity() {

    /**
     * ViewBinding 的初始化必须在 FragmentActivity 的 onCreate(savedInstanceState) 之后，
     *    之前获取到的 LayoutInflater 填充布局可能会出错
     */
    protected var viewBinding: T? = null
        private set

    abstract fun createViewBinding(inflater: LayoutInflater): T

    abstract fun T.onViewCreated(savedInstanceState: Bundle?)

    override val layoutResId: Int?
        get() = null

    override fun getLayoutView(): View {
        createViewBinding(layoutInflater).run {
            viewBinding = this
            return this.root
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        viewBinding?.onViewCreated(savedInstanceState)
    }
}