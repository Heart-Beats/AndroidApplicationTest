package com.example.zhanglei.myapplication

/**
 * @Author  张磊  on  2021/04/08 at 17:40
 * Email: 913305160@qq.com
 */
object Constant {
    /**
     * 插件 apk/zip 路径
     */
    const val KEY_PLUGIN_ZIP_PATH = "pluginZipPath"

    /**
     * partKey 用来区分入口， 用来实现多个插件不同的加载
     */
    const val KEY_PLUGIN_PART_KEY = "KEY_PLUGIN_PART_KEY"

    /**
     * 启动的插件 Activity 或 Service 路径
     */
    const val KEY_CLASSNAME = "KEY_CLASSNAME"

    /**
     * 需要传入到启动插件里的参数
     */
    const val KEY_EXTRAS = "KEY_EXTRAS"

    /**
     * 需要直接启动插件的容器类型： Activity 或者 Service
     */
    const val KEY_FROM_ID = "KEY_FROM_ID"

    /**
     * 打开 Activity 传入的 formId
     */
    const val FROM_ID_START_ACTIVITY = 1001L

    /**
     * 打开 Service 传入的 formId
     */
    const val FROM_ID_CALL_SERVICE = 1002L

    /**
     *  启动 Intent 的 action
     */
    const val KEY_INTENT_ACTION = "KEY_INTENT_ACTION"
}