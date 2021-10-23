package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zhanglei.myapplication.databinding.FragmentOpenCameraBinding
import com.example.zhanglei.myapplication.fragments.base.ViewBindingBaseFragment
import com.hl.utils.onClick


class OpenCameraFragment : ViewBindingBaseFragment<FragmentOpenCameraBinding>() {

	override fun createViewBinding(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): FragmentOpenCameraBinding {
		return FragmentOpenCameraBinding.inflate(inflater, container, false)
	}

	override fun FragmentOpenCameraBinding.onViewCreated(savedInstanceState: Bundle?) {
		var isBackOpen = true

		val cameraXFragment = childFragmentManager.findFragmentByTag("cameraX") as CameraXFragment

		this.openCamera.onClick {
			isBackOpen = !isBackOpen
			cameraXFragment.cameraType = if (isBackOpen) {
				openCamera.text = "打开前置摄像头"
				CameraType.CAMERA_BACK
			} else {
				openCamera.text = "打开后置摄像头"
				CameraType.CAMERA_FRONT
			}
		}
	}

}