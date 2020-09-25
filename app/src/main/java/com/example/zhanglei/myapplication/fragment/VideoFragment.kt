package com.example.zhanglei.myapplication.fragment

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.elvishew.xlog.XLog
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.util.initPlayer
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.android.synthetic.main.fragment_video.*

class VideoFragment : BaseFragment() {

	private lateinit var orientationUtils: OrientationUtils

	override val layoutResId: Int
		get() = R.layout.fragment_video


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		toolbar?.title = "视频播放测试"

		// 设置 Activity 根据传感器变化
		// requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

		val url = "https://shanhao-app-1259195890.cos.ap-shanghai.myqcloud.com/app-resources/merchant/videos.voice-manual/sample.mp4"

		initPlayer(video_player, url)
	}

	private fun initPlayer(videoPlayer: StandardGSYVideoPlayer?, url: String) {

		//外部辅助的旋转，帮助全屏
		orientationUtils = OrientationUtils(requireActivity(), videoPlayer)

		videoPlayer?.initPlayer(orientationUtils, url)

	}

	override fun onPause() {
		super.onPause()
		video_player.onVideoPause()
	}


	override fun onResume() {
		super.onResume()
		video_player.onVideoResume()
	}

	override fun onDestroy() {
		super.onDestroy()
		GSYVideoManager.releaseAllVideos()
		orientationUtils.releaseListener()
	}

	override fun onBackPressed(): OnBackPressedCallback.() -> Unit {
		return {
			back()
		}
	}


	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		XLog.d("配置改变")
		// 需要播放自动随屏幕旋转 全屏/退出全屏
		// video_player.onConfigurationChanged(requireActivity(), newConfig, orientationUtils, true, true)
	}

	private fun back() {
		//先返回正常状态
		if (orientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			video_player.fullscreenButton.performClick()
		} else {
			//释放所有
			video_player.setVideoAllCallBack(null)
			findNavController().popBackStack()
		}
	}
}