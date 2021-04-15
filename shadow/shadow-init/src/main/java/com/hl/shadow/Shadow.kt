package com.hl.shadow

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.webkit.WebView
import android.widget.Toast
import com.hl.shadow.manager.MyPluginManagerUpdater
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicPluginManager
import com.tencent.shadow.dynamic.host.DynamicRuntime
import com.tencent.shadow.dynamic.host.PluginManager
import java.io.File
import java.util.concurrent.Future

/**
 * @Author  张磊  on  2021/04/09 at 16:34
 * Email: 913305160@qq.com
 */
object Shadow {

    /**
     * 这个PluginManager对象在Manager升级前后是不变的。它内部持有具体实现，升级时更换具体实现。
     */
    private var sPluginManager: PluginManager? = null

    fun getPluginManager(): PluginManager? {
        return sPluginManager
    }

    fun initDynamicPluginManager(application: Application) {
        //Log接口Manager也需要使用，所以主进程也初始化。
        LoggerFactory.setILoggerFactory(AndroidLoggerFactory.getInstance())

        if (isProcess(application, ":plugin")) {
            //在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的 runtime，
            //为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
            //因此这里恢复加载上一次的runtime
            DynamicRuntime.recoveryRuntime(application)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                WebView.setDataDirectorySuffix("plugin")
            }

            println("当前为插件进程--------------------")
        }

        val pluginManagerPath = "/data/local/tmp/my-PluginManager.apk" //PluginManager.apk文件路径
        // val pluginManagerPath = "/data/local/tmp/sample-manager-debug.apk" //PluginManager.apk文件路径
        // val pluginManagerPath = "/sdcard/my-PluginManager.apk" //PluginManager.apk文件路径
        File(pluginManagerPath).run {
            if (this.exists()) {

                val myPluginManagerUpdater = MyPluginManagerUpdater(this)
                val needWaitingUpdate = (myPluginManagerUpdater.wasUpdating() //之前正在更新中，暗示更新出错了，应该放弃之前的缓存
                        || myPluginManagerUpdater.latest == null) //没有本地缓存

                val update: Future<File>? = myPluginManagerUpdater.update()
                if (needWaitingUpdate) {
                    try {
                        update?.get() //这里是阻塞的，需要业务自行保证更新Manager足够快。
                    } catch (e: Exception) {
                        throw RuntimeException("Sample程序不容错", e)
                    }
                }
                sPluginManager = DynamicPluginManager(myPluginManagerUpdater)
            } else {
                Toast.makeText(application, "PluginManager APK 未找到", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isProcess(context: Context, processName: String): Boolean {
        var currentProcessName = ""
        val manager = context.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                currentProcessName = processInfo.processName
                break
            }
        }

        return currentProcessName.endsWith(processName)
    }
}