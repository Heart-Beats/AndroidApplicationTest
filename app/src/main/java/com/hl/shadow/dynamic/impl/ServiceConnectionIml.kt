package com.hl.shadow.dynamic.impl

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import com.example.zhanglei.myapplication.MyApplication
import com.hl.pluginaidl.Person
import com.hl.pluginaidl.PluginAidlInterface
import com.hl.pluginaidl.PluginAidlListener

/**
 * @author  张磊  on  2021/06/24 at 11:09
 * Email: 913305160@qq.com
 *
 * 该类类名固定，用于接收 Shadow DynamicManager 绑定 Service 时的回调，可在其中通过 AIDL 接口实现宿主与插件双向通信
 *      通知顺序为： 插件 -----> Manager ----> 宿主
 */
class ServiceConnectionIml : ServiceConnection {

	private val mainHandler = Handler(Looper.getMainLooper())

	override fun onServiceConnected(name: ComponentName, service: IBinder) {

		/**
		 * asInterface: 用于将服务端的 Binder对象转换成客户端所需的 AIDL 接口类型的对象，这种转换过程是区分进程的
		 *      【如果客户端和服务端位于同一进程，那么此方法返回的就是服务端的 Stub 对象本身，
		 *          否则返回的是系统封装后的 Stub.proxy 对象】
		 */
		val pluginAidlInterface = PluginAidlInterface.Stub.asInterface(service)

		pluginAidlInterface.setPluginListener(object : PluginAidlListener.Stub() {
			override fun onOpenActivity(result: Boolean, openActivityName: String?) {
				println(
					"onOpenActivity --------> result = [${result}], openActivityName = " +
							"[${openActivityName}]"
				)
			}

			override fun onTransPerson(person: Person) {
				mainHandler.post {
					Toast.makeText(MyApplication.getInstance(), "收到客户端发送的数据$person", Toast.LENGTH_SHORT).show()
				}
			}
		})

		pluginAidlInterface.openActivity("com.hl.myplugin.MainActivity", null)
	}

	override fun onServiceDisconnected(name: ComponentName?) {
		println("onServiceDisconnected ----> name = [${name}]")
	}
}