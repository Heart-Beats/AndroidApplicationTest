package com.example.zhanglei.myapplication.fragments

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.elvishew.xlog.XLog
import com.example.zhanglei.myapplication.databinding.FragmentVideoBinding
import com.example.zhanglei.myapplication.fragments.base.ViewBindingBaseFragment
import com.hl.utils.initPlayer
import com.hl.utils.reqPermissions
import com.hl.utils.widgets.MyStandardGSYVideoPlayer

class VideoFragment : ViewBindingBaseFragment<FragmentVideoBinding>() {

	private var video_player: com.hl.utils.widgets.MyStandardGSYVideoPlayer? = null

	override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentVideoBinding {
		return FragmentVideoBinding.inflate(inflater, container, false)
	}

	override fun FragmentVideoBinding.onViewCreated(savedInstanceState: Bundle?) {
		toolbar?.title = "视频播放测试"

		// 设置 Activity 根据传感器变化
		// requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)

		val url = "https://shanhao-app-1259195890.cos.ap-shanghai.myqcloud.com/app-resources/merchant/videos.voice-manual/sample.mp4"
		// val url = "http://7xjmzj.com1.z0.glb.clouddn.com/20171026175005_JObCxCE2.mp4"

		this@VideoFragment.video_player = this.videoPlayer
		initPlayer(videoPlayer, url)
	}

	private fun initPlayer(videoPlayer: com.hl.utils.widgets.MyStandardGSYVideoPlayer, url: String) {
		reqPermissions(allGrantedAction = {
			videoPlayer.initPlayer(this, url)
		})
	}

	override fun onBackPressed() {
		back()
	}


	override fun onConfigurationChanged(newConfig: Configuration) {
		super.onConfigurationChanged(newConfig)
		XLog.d("配置改变")
		// 需要播放自动随屏幕旋转 全屏/退出全屏
		// video_player.onConfigurationChanged(requireActivity(), newConfig, orientationUtils, true, true)
	}

	private fun back() {
		//先返回正常状态
		if (video_player?.orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ||
				video_player?.isIfCurrentIsFullscreen == true) {
			video_player?.onBackFullscreen()
		} else {
			//释放所有
			video_player?.setVideoAllCallBack(null)
			findNavController().popBackStack()
		}
	}
}