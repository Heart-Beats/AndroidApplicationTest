package com.example.zhanglei.myapplication

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog

/**
 * @Author  张磊  on  2020/09/01 at 12:39
 * Email: 913305160@qq.com
 */
class MyApplication:Application() {

	companion object {
		private lateinit var mApplication: Application

		fun getInstance(): Application {
			return mApplication
		}
	}

	override fun onCreate() {
		super.onCreate()
		mApplication = this

		val logConfig = LogConfiguration.Builder()
				.logLevel(if (isEnvTDebug()) {
					LogLevel.ALL
				} else {
					LogLevel.INFO
				})
				.st(1)
				.addInterceptor {
					//......
					it
				}
				.build()
		XLog.init(logConfig)
	}

	private fun isEnvTDebug(): Boolean {
		return BuildConfig.DEBUG
	}
}