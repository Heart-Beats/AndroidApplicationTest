package com.example.zhanglei.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.camera2.internal.Camera2CameraInfoImpl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.fragments.base.BaseFragment
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author  张磊  on  2021/06/07 at 10:48
 * Email: 913305160@qq.com
 */
class CameraXFragment : BaseFragment() {

	private companion object {
		private const val TAG = "CameraXFragment"
		private const val REQUEST_CODE_PERMISSIONS = 10
		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
	}

	var cameraId: String = "0"
		set(value) {
			cameraIdLiveData.value = value
			field = value
		}

	private val cameraIdLiveData by lazy {
		MutableLiveData(cameraId)
	}

	private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()


	override val layoutResId: Int
		get() = R.layout.fragment_camera_x

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// 请求相机权限
		if (allPermissionsGranted()) {
			startCamera(view)
		} else {
			ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
		}
	}

	private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
		ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
	}


	@SuppressLint("RestrictedApi", "UnsafeOptInUsageError")
	private fun startCamera(view: View) {
		//用于将摄像机的生命周期绑定到生命周期所有者，省去打开和关闭相机的任务
		val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

		val runnable: () -> Unit = {
			// ProcessCameraProvider 用于将相机的生命周期绑定到 LifecycleOwner 应用程序的过程中
			val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

			// 初始化 Preview 对象，然后给其设置取景器中的 surfaceProvider
			val viewFinder = view.findViewById<PreviewView>(R.id.viewFinder)
			val preview = Preview.Builder()
				.build()
				.also {
					it.setSurfaceProvider(viewFinder.surfaceProvider)
				}

			try {
				cameraIdLiveData.observe(viewLifecycleOwner) { cameraId ->
					val cameraSelector = CameraSelector.Builder()
						//	从返回的摄像头过滤列表中取第一个使用
						.addCameraFilter { cameraInfoList ->
							cameraInfoList.filter {
								val camera2CameraInfoImpl = it as? Camera2CameraInfoImpl
								camera2CameraInfoImpl?.cameraId == cameraId
							}
						}
						.build()

					// 重新绑定之前解绑所有用例
					cameraProvider.unbindAll()
					// 绑定用例到相机，绑定时可传入多个 UseCase
					cameraProvider.bindToLifecycle(this, cameraSelector, preview)
				}
			} catch (exc: Exception) {
				Log.e(TAG, "startCamera: 用例绑定失败")
			}
		}
		cameraProviderFuture.addListener(runnable, ContextCompat.getMainExecutor(requireContext()))
	}

	override fun onDestroy() {
		cameraExecutor.shutdown()
		super.onDestroy()
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
		if (requestCode == REQUEST_CODE_PERMISSIONS) {
			if (allPermissionsGranted()) {
				startCamera(requireView())
			} else {
				Toast.makeText(requireContext(), "权限未被用户授予", Toast.LENGTH_SHORT).show()

				parentFragmentManager.beginTransaction().also {
					it.remove(this)
				}.commit()
			}
		}
	}
}