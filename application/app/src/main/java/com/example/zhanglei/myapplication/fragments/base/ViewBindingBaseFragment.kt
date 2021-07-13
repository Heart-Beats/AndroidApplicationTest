package com.example.zhanglei.myapplication.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * @Author  张磊  on  2021/03/02 at 12:44
 * Email: 913305160@qq.com
 */
abstract class ViewBindingBaseFragment<T : ViewBinding> : BaseFragment() {

    protected var viewBinding: T? = null
        private set

    override val layoutResId: Int?
        get() = null


    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): T
    abstract fun T.onViewCreated(savedInstanceState: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = createViewBinding(inflater, container, savedInstanceState).also {
            layoutView = it.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding?.onViewCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Fragment 的存在时间比其视图长。请务必在 Fragment 的 onDestroyView() 方法中清除对绑定类实例的所有引用。
        viewBinding = null
    }
}