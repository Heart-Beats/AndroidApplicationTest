package com.example.zhanglei.myapplication.utils.navigationutil

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination
import com.elvishew.xlog.XLog
import com.example.zhanglei.myapplication.utils.ReflectHelper
import java.util.regex.Pattern

/**
 * @Author  张磊  on  2021/03/16 at 18:24
 * Email: 913305160@qq.com
 */

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