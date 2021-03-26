package com.hl.utils

import android.content.pm.ActivityInfo
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import com.hl.utils.widgets.MyStandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.utils.Debuger
import tv.danmaku.ijk.media.player.IjkMediaPlayer


/**
 * @Author  张磊  on  2020/09/25 at 19:18
 * Email: 913305160@qq.com
 */

fun MyStandardGSYVideoPlayer.initPlayer(lifecycleOwner: LifecycleOwner, url: String, videoName: String = "测试视频",
										needTitle: Boolean = true) {
	lifecycleOwner.lifecycle.addObserver(this)

	//初始化不打开外部的旋转
	this.orientationUtils?.isEnable = false

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
			// .setShowFullAnimation(true)
			// .setNeedLockFull(true)
			.setUrl(url)
			// .setCacheWithPlay(true)
			.setVideoTitle(videoName)
			.setVideoAllCallBack(object : GSYSampleCallBack() {
				override fun onPrepared(url: String?, vararg objects: Any?) {
					//开始播放了才能旋转和全屏
					this@initPlayer.orientationUtils?.isEnable = true
					// startAfterPrepared()
				}
			})
			.setLockClickListener { _, lock ->
				this.orientationUtils?.isEnable = !lock
			}.build(this)

	this.apply {

		val visibility = when (needTitle) {
			true -> View.VISIBLE
			else -> View.GONE
		}
		this.titleTextView.visibility = visibility

		this.fullscreenButton?.setOnClickListener {
			//横屏/竖屏切换
			this.orientationUtils?.resolveByClick()

			//第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusBar
			this.startWindowFullscreen(this.context, true, true)
		}

		this.backButton?.visibility = View.GONE
		this.backButton?.setOnClickListener {
			//先返回正常状态
			if (this.orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || this.isIfCurrentIsFullscreen) {
				this.onBackFullscreen()
			} else {
				//释放所有
				this.setVideoAllCallBack(null)
				this.findNavController().popBackStack()
			}
		}
	}
}