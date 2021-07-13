package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver

/**
 * @author  张磊  on  2021/06/18 at 12:47
 * Email: 913305160@qq.com
 */
class TestResultReceiver(handler: Handler) : ResultReceiver(handler) {

	override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
		if (resultCode == 0) {
			println("收到的数据 == ${resultData.getString("我是参数1")}")
		}
	}
}