package com.hl.shadow

import android.content.Context
import com.hl.shadow.logger.AndroidLoggerFactory
import com.hl.shadow.managerupdater.MyPluginManagerUpdater
import com.hl.shadow.pluginmanager.MyPluginManager
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicPluginManager
import com.tencent.shadow.dynamic.host.PluginManager
import com.tencent.shadow.dynamic.host.PluginManagerImpl
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
    private var dynamicPluginManager: PluginManager? = null


    /**
     * 静态实现的插件管理类
     */
    private var pluginManagerImpl: PluginManagerImpl? = null


    /**
     * 获取静态的 PluginManager， 无需动态 APK 进行加载
     *
     * @param  context           上下文对象
     * @return PluginManager
     */
    fun getMyPluginManager(context: Context): PluginManager? {
        try {
            LoggerFactory.getILoggerFactory()
        } catch (e: Exception) {
            LoggerFactory.setILoggerFactory(AndroidLoggerFactory.getInstance())
        }

        if (pluginManagerImpl == null) {
            // 动态获取是通过反射机制获取的
            // val className = "com.tencent.shadow.dynamic.impl.ManagerFactoryImpl"
            // val newInstance = Class.forName(className).newInstance()
            // pluginManagerImpl = ManagerFactory::class.cast(newInstance).buildManager(context)
            pluginManagerImpl = MyPluginManager(context)
        }
        return pluginManagerImpl
    }


    /**
     * 初始化动态的插件 Manager，可以动态更新的
     *
     * @param pluginManagerPath    插件 Manager 所在的绝对路径
     */
    fun initDynamicPluginManager(pluginManagerPath: String) {
        if (dynamicPluginManager != null) {

        }

        //Log接口Manager也需要使用，所以主进程也初始化。
        LoggerFactory.setILoggerFactory(AndroidLoggerFactory.getInstance())
        File(pluginManagerPath).run {
            if (this.exists()) {

                val myPluginManagerUpdater = MyPluginManagerUpdater(this)
                val needWaitingUpdate = (myPluginManagerUpdater.wasUpdating() //之前正在更新中，暗示更新出错了，应该放弃之前的缓存
                        || myPluginManagerUpdater.latest == null) //没有本地缓存

                val update: Future<File>? = myPluginManagerUpdater.update()
                if (needWaitingUpdate) {
                    try {
                        update?.get() //这里是阻塞的，需要业务自行保证更新 Manager 足够快。
                    } catch (e: Exception) {
                        throw RuntimeException("Sample程序不容错", e)
                    }
                }
                dynamicPluginManager = DynamicPluginManager(myPluginManagerUpdater)
            } else {
                val logger = LoggerFactory.getLogger(this.javaClass)
                if (logger.isDebugEnabled) {
                    logger.debug("PluginManager APK 未找到")
                }

                throw ExceptionInInitializerError("PluginManager APK 未找到")
            }
        }
    }

    /**
     * 获取动态的 PluginManager ， [initDynamicPluginManager] 若调用成功，则返回 null
     */
    fun getDynamicPluginManager() = dynamicPluginManager
}