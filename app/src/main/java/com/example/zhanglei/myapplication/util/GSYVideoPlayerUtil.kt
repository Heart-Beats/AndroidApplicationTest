package com.example.zhanglei.myapplication.util

import android.content.pm.ActivityInfo
import android.widget.ImageView
import androidx.navigation.findNavController
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * @Author  张磊  on  2020/09/25 at 19:18
 * Email: 913305160@qq.com
 */

fun GSYBaseVideoPlayer.initPlayer(orientationUtils: OrientationUtils, url: String) {
	//外部辅助的旋转，帮助全屏

	//初始化不打开外部的旋转
	orientationUtils.isEnable = false

	Debuger.disable()
	IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT)

	val imageView = ImageView(this.context)
	imageView.loadFirstFrameCover(url)

	GSYVideoOptionBuilder()
			.setThumbImageView(imageView)
			.setIsTouchWiget(true)
			.setRotateViewAuto(false)
			.setLockLand(false)
			.setAutoFullWithSize(true)
			.setShowFullAnimation(true)
			// .setNeedLockFull(true)
			.setUrl(url)
			.setCacheWithPlay(false)
			.setVideoTitle("测试视频")
			.setVideoAllCallBack(object : GSYSampleCallBack() {
				override fun onPrepared(url: String?, vararg objects: Any?) {
					startAfterPrepared()
				}
			})
			.setLockClickListener { _, lock ->
				orientationUtils.isEnable = !lock
			}.build(this)

	this.apply {

		fullscreenButton?.setOnClickListener {
			//横屏/竖屏切换
			// orientationUtils?.resolveByClick()

			//第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusBar
			this.startWindowFullscreen(this.context, true, true)
		}

		backButton?.setOnClickListener {
			//先返回正常状态
			if (orientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
				this.fullscreenButton.performClick()
			} else {
				//释放所有
				this.setVideoAllCallBack(null)
				this.findNavController().popBackStack()
			}
		}
	}
}