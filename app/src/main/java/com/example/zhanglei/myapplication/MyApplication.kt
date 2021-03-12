package com.example.zhanglei.myapplication

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.example.zhanglei.myapplication.widgets.refresh.CommonRefreshHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import io.dcloud.common.util.RuningAcitvityUtil
import io.dcloud.feature.sdk.DCSDKInitConfig
import io.dcloud.feature.sdk.DCUniMPSDK
import io.dcloud.feature.sdk.MenuActionSheetItem


/**
 * @Author  张磊  on  2020/09/01 at 12:39
 * Email: 913305160@qq.com
 */
class MyApplication : Application() {

	companion object {
		private lateinit var mApplication: Application

		fun getInstance(): Application {
			return mApplication
		}
	}

	override fun onCreate() {
		super.onCreate()
		mApplication = this

		// 非小程序进程
		if (!RuningAcitvityUtil.getAppName(baseContext).contains("io.dcloud.unimp")) {
			//请在此处初始化其他三方SDK
			// 非小程序进程初始化其他三方SDK
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
			initRefreshLayout()
		}

		initUniApp {
			val item = MenuActionSheetItem("关于", "gy")
			val sheetItems: MutableList<MenuActionSheetItem> = ArrayList()
			sheetItems.add(item)

			this.setCapsule(true)
					.setMenuDefFontSize("16px")
					.setMenuDefFontColor("#ff00ff")
					.setMenuDefFontWeight("normal")
					.setMenuActionSheetItems(sheetItems)
		}
	}

	private fun isEnvTDebug(): Boolean {
		return BuildConfig.DEBUG
	}

	private fun initRefreshLayout() {
		//SmartRefreshLayout 默认配置，后续可在 XMl 中进行更改
		SmartRefreshLayout.setDefaultRefreshInitializer { _, refreshLayout ->
			refreshLayout.layout.tag = "close egg" // 关闭下来刷新死拉彩蛋

			refreshLayout.setHeaderMaxDragRate(1.5f) //最大下拉高度与Header标准高度的倍数
			refreshLayout.setHeaderHeight(80f)  //Header标准高度（显示下拉高度>=标准高度 触发刷新), 默认高度：100dp
			// refreshLayout.setReboundDuration(500) //回弹动画时长（毫秒）默认: 300ms
			// refreshLayout.setHeaderInsetStart(200f) //设置 Header 起始位置偏移量
			// refreshLayout.setPrimaryColorsId(R.color.colorAccent)

			/*	下拉距离 与 HeaderHeight 的比率达到此值时将会触发刷新（默认1，即下拉距离等于头部高度触发刷新，但若最大下拉高度等于头部高度，
                实际上是无法拉满的，因此几乎不会触发刷新动画，即 onStartAnimator 事件）*/
			// refreshLayout.setHeaderTriggerRate(0.6f)

			refreshLayout.setFooterMaxDragRate(1.5f) //最大下拉高度与footer标准高度的倍数
			refreshLayout.setFooterHeight(80f) //Footer标准高度（显示下拉高度>=标准高度 触发刷新), 默认高度：100dp
		}

		//SmartRefreshLayout 设置默认刷新头
		SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->

			CommonRefreshHeader(context).apply {
				// this.setPullAnimation(rawRes = R.raw.refresh_new_gray_pull)
				// this.setRefreshAnimation(rawRes = R.raw.refresh_new_gray)
			}
		}

		//目前全局创建 footer 将会导致 footer 的布局被创建两次 ---> 使用view动画会有问题
		//SmartRefreshLayout 设置默认刷新尾
		// SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
		//     SimpleLottieRefreshFooter(context)
		// }
	}

	private fun initUniApp(uniSDKInitConfigBuilder: DCSDKInitConfig.Builder.() -> Unit) {
		val config = DCSDKInitConfig.Builder().apply(uniSDKInitConfigBuilder).build()
		DCUniMPSDK.getInstance().initialize(this, config) {
			if (it) {
				println("初始化 UniApp 成功")
			} else {
				println("初始化 UniApp 失败")
			}
		}
	}
}

val uniAppSdk = if (DCUniMPSDK.getInstance().isInitialize) DCUniMPSDK.getInstance() else null

