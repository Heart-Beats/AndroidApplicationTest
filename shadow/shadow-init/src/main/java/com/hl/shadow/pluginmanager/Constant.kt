package com.hl.pluginmanager

/**
 * @Author  张磊  on  2021/04/08 at 17:40
 * Email: 913305160@qq.com
 */
object Constant {
    /**
     * PluginManager 实现的别名, 用于区分不同PluginManager实现的数据存储路径
     */
    const val PLUGIN_MANAGER_NAME = "my-plugin-manager"

    /**
     * demo插件so的abi
     */
    const val ABI = ""

    /**
     * 宿主中注册的PluginProcessService实现的类名
     */
    const val PLUGIN_PROCESS_SERVICE_NAME =
        "com.hl.shadow.MainPluginProcessService"

    /**
     * 插件 apk/zip 路径
     */
    const val KEY_PLUGIN_ZIP_PATH = "pluginZipPath"

    /**
     * partKey 用来区分入口， 用来实现多个插件不同的加载
     */
    const val KEY_PLUGIN_PART_KEY = "KEY_PLUGIN_PART_KEY"

    /**
     * 启动的插件 Activity 路径
     */
    const val KEY_ACTIVITY_CLASSNAME = "KEY_ACTIVITY_CLASSNAME"

    /**
     * 需要传入到启动插件里的参数
     */
    const val KEY_EXTRAS = "KEY_EXTRAS"

    /**
     * 打开 Activity 传入的 formId
     */
    const val FROM_ID_START_ACTIVITY = 1001L

    /**
     * 打开 Service 传入的 formId
     */
    const val FROM_ID_CALL_SERVICE = 1002L
}