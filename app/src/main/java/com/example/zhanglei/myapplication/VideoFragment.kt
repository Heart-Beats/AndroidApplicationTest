package com.example.zhanglei.myapplication

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.elvishew.xlog.XLog
import com.example.zhanglei.myapplication.fragment.BaseFragment
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.synthetic.main.fragment_video.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class VideoFragment : BaseFragment() {

	private lateinit var orientationUtils: OrientationUtils

	override val layoutResId: Int
		get() = R.layout.fragment_video


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT)

		val source1 = "https://shanhao-app-1259195890.cos.ap-shanghai.myqcloud.com/app-resources/merchant/videos.voice-manual/sample.mp4"
		video_player.setUp(source1, true, "测试视频")

		// //增加title
		// video_player.titleTextView.visibility = View.VISIBLE
		// //设置返回键
		// video_player.backButton.visibility = View.VISIBLE
		//设置旋转
		orientationUtils = OrientationUtils(activity, video_player)
		//设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
		video_player.fullscreenButton.setOnClickListener {
			orientationUtils.resolveByClick()

			video_player.startWindowFullscreen(requireContext(), true, true)
		}
		//是否可以滑动调整
		video_player.setIsTouchWiget(true)
		//设置返回按键功能
		video_player.backButton.setOnClickListener {
			back()
		}
		video_player.startPlayLogic()
		video_player.setUp(source1, true, "测试视频")
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
		video_player.onConfigurationChanged(requireActivity(), newConfig, orientationUtils, true, true)
	}

	private fun back() {
		XLog.d("返回")
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