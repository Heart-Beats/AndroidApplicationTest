package com.example.zhanglei.myapplication.activities.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hl.utils.StatusBarUtil
import com.hl.utils.initInsetPadding

abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutResId: Int?

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    /**
     * 该方法在 ViewBindingBaseActivity 中使用，确保 super.onCreate(savedInstanceState) 之后填充 ViewBinding 的视图
     */
    protected open fun getLayoutView(): View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutResId?.run {
            setContentView(this)
        }
        getLayoutView()?.run {
            setContentView(this)
        }
        initInsetPadding(top = true)
        com.hl.utils.StatusBarUtil.transparencyBar(window)
        com.hl.utils.StatusBarUtil.statusBarLightMode(window)
        com.hl.utils.StatusBarUtil.setStatusBarColor(this, Color.WHITE)
        onViewCreated(savedInstanceState)
    }


    protected fun Context.startActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }
}