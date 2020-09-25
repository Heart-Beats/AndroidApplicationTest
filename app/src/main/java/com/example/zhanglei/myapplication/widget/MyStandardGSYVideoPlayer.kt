package com.example.zhanglei.myapplication.widget

import android.content.Context
import android.util.AttributeSet
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

/**
 * @Author  张磊  on  2020/09/25 at 18:58
 * Email: 913305160@qq.com
 */
class MyStandardGSYVideoPlayer : StandardGSYVideoPlayer {
	constructor(context: Context?) : super(context)
	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


	override fun init(context: Context) {
		super.init(context)
		// val url = "https://shanhao-app-1259195890.cos.ap-shanghai.myqcloud.com/app-resources/merchant/videos.voice-manual/sample.mp4"
		// this.setUp(url, false, "")
		//
		// isStartAfterPrepared = false
		startPrepare()
	}
}