package com.example.zhanglei.myapplication.util.download

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.zhanglei.myapplication.MyApplication
import com.example.zhanglei.myapplication.util.gsonParseJson2List
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.HttpURLConnection

/**
 * @Author  张磊  on  2020/11/04 at 15:03
 * Email: 913305160@qq.com
 */
internal class DownloadTask(private val downloadUrl: String, private val maxDownloadCore: Int = 1) {

	companion object {
		private const val TAG = "DownloadTask"

		private const val SUB_DOWN_LOAD_TASKS = "下载任务"
	}

	private val subDownLoadTasks = mutableListOf<SubDownLoadTask>()
	private val downloadListener = DownloadStatusListener()

	private var fileSize = 0L

	fun startDownload() {

		MainScope().launch {

			val okHttpClient = OkHttpClient()
			val requestBuilder = Request.Builder().url(downloadUrl)
			fileSize = async(Dispatchers.IO) {
				okHttpClient.newCall(requestBuilder.build()).execute().body?.contentLength() ?: 0
			}.await()

			requestBuilder.addHeader("RANGE", "bytes=0-")
			val statusCode = async(Dispatchers.IO){
				okHttpClient.newCall(requestBuilder.build()).execute().code
			}.await()

			Log.d(TAG, "startDownload: fileSize == $fileSize , statusCode = $statusCode")

			val saveFile = getSaveFile()

			if (!existTask() || !saveFile.exists()) {
				if (!saveFile.exists()) {
					saveFile.createNewFile()
				}

				if (statusCode == HttpURLConnection.HTTP_PARTIAL) {
					//服务端支持断点续传
					initPartialSubTask(saveFile)
				} else {
					initSubTask(saveFile)
				}
			}
			startAsyncDownload()
		}
	}

	private fun getSaveFile(): File {
		val fileName = downloadUrl.split("/").last()
		val file = MyApplication.getInstance().getExternalFilesDir(null)
		checkNotNull(file) {
			"下载文件存放目录为空"
		}
		return File(file, fileName)
	}

	private fun existTask(): Boolean {
		val subDownLoadTasksString = getSharedPreferences().getString(SUB_DOWN_LOAD_TASKS, "")
		val subDownLoadTasks = gsonParseJson2List<SubDownLoadTask>(subDownLoadTasksString)
		return if (subDownLoadTasks?.isNotEmpty() == true && subDownLoadTasks[0].downLoadUrl == downloadUrl) {
			//如果保存的下载任务不为空，同时下载地址与此次任务相同时 ======》 任务已存在
			this.subDownLoadTasks.addAll(subDownLoadTasks)
			true
		} else {
			false
		}
	}

	private fun initPartialSubTask(file: File) {
		val partSizeList = mutableListOf<Long>()
		for (i in 0 until maxDownloadCore) {
			// 分配大小原则：平均分配，剩下的依次从头添加1 ， 如：4B 分配给 3个 ：[2B,1B,1B]
			val partSize = fileSize / maxDownloadCore + if (i < fileSize % maxDownloadCore) 1 else 0
			partSizeList.add(partSize)
		}

		subDownLoadTasks.clear()
		partSizeList.forEachIndexed { index, _ ->
			//开始位置：自身在分配大小list 位置的前面所有项和（不包括自身）， 如： [2,1,1] ==》 [0,2,3]
			val startPos = partSizeList.take(index).reduceOrNull { sum, size -> sum + size } ?: 0
			//结束位置：自身在分配大小list 位置的前面所有项和-1（包括自身）， 如： [2,1,1] ==》 [1,2,3]
			val endPos = partSizeList.take(index + 1).reduce { sum, size -> sum + size } - 1

			val subDownLoadTask = SubDownLoadTask(downloadUrl, startPos = startPos, endPos = endPos, saveFile = file,
					downloadStatusListener = downloadListener)
			subDownLoadTasks.add(subDownLoadTask)
		}
	}

	/**
	 * 不支持断点续传，单线程下载任务
	 */
	private fun initSubTask(file: File) {
		val subDownLoadTask = SubDownLoadTask(downloadUrl, saveFile = file, downloadStatusListener = downloadListener)
		subDownLoadTasks.clear()
		subDownLoadTasks.add(subDownLoadTask)
	}

	private fun startAsyncDownload() {
		subDownLoadTasks.forEach {
			if (it.downloadStatus != DownloadStatus.DOWNLOAD_COMPLETE) {
				it.startDownLoad()
			}
		}
	}

	private inner class DownloadStatusListener : OnDownloadStatusListener {

		override fun downloadStatusChage(status: DownloadStatus) {
			when (status) {
				DownloadStatus.DOWNLOAD_ERROR -> {
					subDownLoadTasks.forEach {
						if (it.downloadStatus != DownloadStatus.DOWNLOAD_CANCEL) {
							it.downloadCancel()
						}
					}

					getSharedPreferences().edit {
						this.putString("SUB_DOWN_LOAD_TASKS", Gson().toJson(subDownLoadTasks))
					}

					DownloadManager.downloadStatusChange(downloadStatus = status)
				}

				DownloadStatus.DOWNLOADING -> {
					val sum = subDownLoadTasks.fold(0L) { sum, subDownLoadTask ->
						sum + subDownLoadTask.currentPos - (subDownLoadTask.startPos ?: 0) + 1
					}

					DownloadManager.downloadStatusChange(downloadStatus = status, progress = (sum / fileSize * 100).toInt())
				}

				DownloadStatus.DOWNLOAD_COMPLETE -> {
					if (subDownLoadTasks.all {
								it.downloadStatus == DownloadStatus.DOWNLOAD_COMPLETE
							}) {
						DownloadManager.downloadStatusChange(downloadStatus = status)
					}

					getSharedPreferences().edit {
						this.putString("SUB_DOWN_LOAD_TASKS", Gson().toJson(subDownLoadTasks))
					}
				}
				DownloadStatus.DOWNLOAD_PAUSE -> {
					DownloadManager.downloadStatusChange(downloadStatus = status)

				}
				DownloadStatus.DOWNLOAD_CANCEL -> {
					DownloadManager.downloadStatusChange(downloadStatus = status)
				}
				else -> {
				}
			}
		}
	}

	private fun getSharedPreferences(): SharedPreferences {
		val instance = MyApplication.getInstance()
		return instance.getSharedPreferences(instance.packageName, Context.MODE_PRIVATE)
	}

}