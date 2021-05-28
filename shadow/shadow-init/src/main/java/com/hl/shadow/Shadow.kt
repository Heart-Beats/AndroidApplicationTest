package com.hl.shadow

import android.content.Context
import com.hl.shadow.logger.AndroidLoggerFactory
import com.hl.shadow.managerupdater.MyPluginManagerUpdater
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicPluginManager
import com.tencent.shadow.dynamic.host.ManagerFactory
import com.tencent.shadow.dynamic.host.PluginManager
import com.tencent.shadow.dynamic.host.PluginManagerImpl
import java.io.File
import java.util.concurrent.Future
import kotlin.reflect.cast

/**
 * @Author  张磊  on  2021/04/09 at 16:34
 * Email: 913305160@qq.com
 */
object Shadow {

    /**
     * 这个PluginManager对象在Manager升级前后是不变的。它内部持有具体实现，升级时更换具体实现。
     */
    private var dynamicPluginManager: PluginManager? = null

    private var pluginManagerImpl: PluginManagerImpl? = null

    /**
     * 获取对应的 PluginManager， 动态 APK 或者静态类加载
     * @param  needDynamic 是否需要动态 PluginManager
     * @param  context 默认为 null，不需要动态 PluginManager 时一定要传入
     * @return PluginManager
     */
    fun getPluginManager(needDynamic: Boolean = true, context: Context? = null): PluginManager? {
        return if (needDynamic) {
            dynamicPluginManager
        } else {
            if (context == null) {
                throw Exception("非动态获取插件 Manager 需要传入 context")
            }

            try {
                LoggerFactory.getILoggerFactory()
            } catch (e: Exception) {
                LoggerFactory.setILoggerFactory(AndroidLoggerFactory.getInstance())
            }

            if (pluginManagerImpl == null) {
                val className = "com.tencent.shadow.dynamic.impl.ManagerFactoryImpl"
                val newInstance = Class.forName(className).newInstance()
                pluginManagerImpl = ManagerFactory::class.cast(newInstance).buildManager(context)
            }
            pluginManagerImpl
        }
    }

    fun initDynamicPluginManager(pluginManagerPath: String) {
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
            }
        }
    }
}