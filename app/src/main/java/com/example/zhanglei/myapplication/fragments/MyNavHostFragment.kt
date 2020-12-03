package com.example.zhanglei.myapplication.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.AnimRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.example.zhanglei.myapplication.R

/**
 * @Author  张磊  on  2020/12/02 at 14:02
 * Email: 913305160@qq.com
 */
class MyNavHostFragment : NavHostFragment() {

    private companion object {
        const val ENTER_ANIM = "enterAnim"
        const val EXIT_ANIM = "exitAnim"
        const val POP_ENTER_ANIM = "popEnterAnim"
        const val POP_EXIT_ANIM = "popExitAnim"
    }

    private var navAnimations: NavAnimations? = null

    override fun createFragmentNavigator(): Navigator<out FragmentNavigator.Destination> {
        return MyFragmentNavigator(requireContext(), childFragmentManager, getContainerId())
    }

    /**
     * 因父类方法私有导致的重写，实现逻辑一致
     */
    private fun getContainerId(): Int {
        val id = id
        return if (id != 0 && id != View.NO_ID) {
            id
        } else R.id.nav_host_fragment_container
    }

    /**
     * 统一设置导航的跳转动画以及返回动画，
     * 若 fragment 子标签对 action 已设置相关动画属性，则最终生效的是 action 中的动画属性
     */
    fun setCommonNavAnimations(navAnimationsInit: NavAnimations.() -> Unit) {
        navAnimations = NavAnimations().apply(navAnimationsInit)
    }

    @Navigator.Name("fragment")
    private inner class MyFragmentNavigator(context: Context, manager: FragmentManager, containerId: Int) :
            FragmentNavigator(context, manager, containerId) {

        override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?, navigatorExtras: Navigator.Extras?): NavDestination? {
            val enterAnim = handleDefaultAnim(navOptions, ENTER_ANIM)
            val exitAnim = handleDefaultAnim(navOptions, EXIT_ANIM)
            val popEnterAnim = handleDefaultAnim(navOptions, POP_ENTER_ANIM)
            val popExitAnim = handleDefaultAnim(navOptions, POP_EXIT_ANIM)

            val newNavOptions = NavOptions.Builder().apply {
                this.setEnterAnim(enterAnim)
                this.setExitAnim(exitAnim)
                this.setPopEnterAnim(popEnterAnim)
                this.setPopExitAnim(popExitAnim)
            }.build()

            println("新的导航选项 == $enterAnim, $exitAnim，$popEnterAnim，$popExitAnim")
            return super.navigate(destination, args, newNavOptions, navigatorExtras)
        }

        private fun handleDefaultAnim(navOptions: NavOptions?, propertyName: String): Int {
            try {
                //使用反射取得所需的属性对应的 get 方法
                val methodName = "get${propertyName[0].toUpperCase()}" + propertyName.substring(1)
                val getPropertyFunction = NavOptions::class.java.getMethod(methodName)

                return if (navOptions == null || getPropertyFunction.invoke(navOptions) == -1) {
                    // 当 navOptions 为空或者 navOptions 未设置动画（解析 action 时动画资源默认值都为 -1 ） ----> 使用共通动画
                    navAnimations?.run {
                        val getAnimFunction = this::class.java.getMethod(methodName)
                        getAnimFunction.invoke(this) as? Int ?: -1
                    } ?: -1
                } else {
                    getPropertyFunction.invoke(navOptions) as? Int ?: -1
                }
            } catch (e: Exception) {
                Log.e("MyFragmentNavigator", "handleDefaultAnim: ", e)
                return -1
            }
        }
    }

    data class NavAnimations(
            @AnimRes var enterAnim: Int? = null,
            @AnimRes var exitAnim: Int? = null,
            @AnimRes var popEnterAnim: Int? = null,
            @AnimRes var popExitAnim: Int? = null,
    )
}