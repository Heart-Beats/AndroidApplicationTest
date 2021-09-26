package com.example.zhanglei.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.camera2.internal.Camera2CameraInfoImpl
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.zhanglei.myapplication.R
import com.example.zhanglei.myapplication.fragments.base.BaseFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author  张磊  on  2021/06/07 at 10:48
 * Email: 913305160@qq.com
 */
enum class CameraType(val cameraId: String) {
	CAMERA_BACK("0"), CAMERA_FRONT("1")
}

class CameraXFragment : BaseFragment() {

	private companion object {
		private const val TAG = "CameraXFragment"
		private const val REQUEST_CODE_PERMISSIONS = 10
		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
	}

	var cameraType = CameraType.CAMERA_BACK
		set(value) {
			cameraIdLiveData.value = value.cameraId
			field = value
		}

	private val cameraIdLiveData by lazy {
		MutableLiveData(cameraType.cameraId)
	}

	private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

	private var imageCapture: ImageCapture? = null

	private var analyzeListener: ((image: ImageProxy) -> Unit)? = null


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

			//初始化 imageCapture 实例
			imageCapture = ImageCapture.Builder()
				.build()

			// 初始化图像分析实例
			val imageAnalyzer = ImageAnalysis.Builder()
				.build().also {
					it.setAnalyzer(cameraExecutor, { image ->
						Thread.sleep(10)

						analyzeListener?.invoke(image)

						// 必须关闭资源，否则不会收到后续回调
						image.close()
					})
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
					cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalyzer)
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

	fun setAnalyzerListener(imageAnalyzer: (imageProxy: ImageProxy) -> Unit) {
		analyzeListener = imageAnalyzer
	}

	fun takePhoto(imageSavedCallback: ImageCapture.OnImageSavedCallback) {
		// 获取图像捕获实例
		val imageCapture = imageCapture ?: return

		// 获取保存照片的格式化路径
		val photoFile = File(
			getOutputDirectory(),
			SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg"
		)

		// 创建一个 OutputFileOptions对象。可以在此对象中指定有关输出结果的方式
		val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

		// 设置图像捕捉监听器，该功能在拍照后触发
		imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), imageSavedCallback)
	}

	private fun getOutputDirectory(): File {
		val mediaDir = requireContext().getExternalMediaDirs().firstOrNull()?.let {
			File(it, requireContext().packageName.split(".").lastOrNull() ?: "").apply {
				mkdirs()
			}
		}
		return if (mediaDir != null && mediaDir.exists()) mediaDir else requireContext().filesDir
	}
}