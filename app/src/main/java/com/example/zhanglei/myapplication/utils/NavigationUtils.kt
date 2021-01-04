package com.example.zhanglei.myapplication.utils

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.AnimRes
import androidx.fragment.app.FragmentManager
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.R
import com.elvishew.xlog.XLog
import java.util.regex.Pattern

/**
 * @Author  张磊  on  2020/12/30 at 16:23
 * Email: 913305160@qq.com
 */

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
            Log.d("MyFragmentNavigator", "navigate: 处理后的动画 == ${navOptions?.toAnimString()}")

            return super.navigate(destination, args, newNavOptions, navigatorExtras)
        }

        /**
         * @param  navOptions 经过 NavController 处理后的导航选项，只要 action 存在，它就不会为 null，即使调用 navigate() 传入 null
         * @param  propertyName 处理的动画属性名
         * @return 动画对应的资源 id , -1 即无动画
         */
        private fun handleDefaultAnim(navOptions: NavOptions?, propertyName: String): Int {
            try {
                //使用反射取得所需的属性对应的 get 方法
                val methodName = "get${propertyName[0].toUpperCase()}" + propertyName.substring(1)
                val getPropertyFunction = NavOptions::class.java.getMethod(methodName)
                Log.d("MyFragmentNavigator", "handleDefaultAnim--$propertyName: navOptions中设置的动画 ==" +
                        " ${navOptions?.toAnimString()}")

                return if (navOptions == null || getPropertyFunction.invoke(navOptions) == NavAnimations.NO_ANIM) {
                    -1
                } else if (getPropertyFunction.invoke(navOptions) == -1) {
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

        fun NavOptions.toAnimString(): String {
            return "NavOptions(mEnterAnim=$enterAnim , mExitAnim=$exitAnim , mPopEnterAnim=$popEnterAnim , mPopExitAnim=$popExitAnim )"
        }
    }
}

data class NavAnimations(
        @AnimRes var enterAnim: Int? = null,
        @AnimRes var exitAnim: Int? = null,
        @AnimRes var popEnterAnim: Int? = null,
        @AnimRes var popExitAnim: Int? = null,
) {
    companion object {
        const val NO_ANIM = 0
    }
}

fun NavController.navigateFromUrl(url: String) {
    val uri = Uri.parse(url)
    if (uri.scheme == "http" || uri.scheme == "https") {
        val newUri = Uri.parse("${uri.scheme}://url=${uri}")

        if (this.graph.hasDeepLink(newUri)) {
            handleMatchDeepLinkForUrl(this, newUri)
            this.navigate(newUri)
        } else {
            XLog.d("未找到与 $newUri 所匹配的深链接页面")
        }
    } else if (uri.scheme == "native") {
        if (this.graph.hasDeepLink(uri)) {
            this.navigate(uri)
        } else {
            XLog.d("未找到与 $uri 所匹配的深链接页面")
        }
    }
}

private fun handleMatchDeepLinkForUrl(navController: NavController, newUri: Uri) {
    //将自身导航以及所有子 NavDestination 添加到列表中 -----> 过滤出匹配给定深链接的 NavDestination
    val navDestinationList = mutableListOf<NavDestination>(navController.graph)
    navController.graph.forEach {
        navDestinationList.add(it)
    }
    navDestinationList.filter {
        it.hasDeepLink(newUri)
    }.forEach {
        val deepLinks = ReflectHelper(NavDestination::class.java).getFiledValue<ArrayList<NavDeepLink>>(it, "mDeepLinks")
        deepLinks?.onEach { navDeepLink ->
            navDeepLink.uriPattern?.run {
                val reflectHelper = ReflectHelper(NavDeepLink::class.java)
                when {
                    this.startsWith("http://") -> Pattern.compile("""^\Qhttp://url=\E(.+?)""")
                    this.startsWith("https://") -> Pattern.compile("""^\Qhttps://url=\E(.+?)""")
                    else -> reflectHelper.getFiledValue<Pattern>(navDeepLink, "mPattern")
                }?.also { pattern ->
                    reflectHelper.setFiledValue(navDeepLink, "mPattern", pattern)
                }
            }
        }
    }
}