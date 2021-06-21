package com.hl.shadow.pluginmanager

import android.content.ComponentName
import android.os.IBinder
import com.tencent.shadow.dynamic.loader.PluginServiceConnection

/**
 * @author  张磊  on  2021/06/21 at 18:35
 * Email: 913305160@qq.com
 */
interface OnPluginServiceConnection {

	fun onServiceConnected(name: ComponentName?, service: IBinder?)

	fun onServiceDisconnected(name: ComponentName?)
}