package com.example.zhanglei.myapplication.util.download

import okhttp3.*
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

/**
 * @Author  张磊  on  2020/11/04 at 15:06
 * Email: 913305160@qq.com
 */
internal data class SubDownLoadTask(
		val downLoadUrl: String,
		val startPos: Long? = null,
		val endPos: Long? = null,
		var currentPos: Long = 0,
		var downloadStatus: DownloadStatus = DownloadStatus.NOT_DOWNLOADED,
		val saveFile: File,
		val downloadStatusListener: OnDownloadStatusListener
) : Callback {

	private val okHttpClient = OkHttpClient()
	private var requestCall: Call? = null

	fun startDownLoad() {
		val request = Request.Builder()
				.url(downLoadUrl).apply {
					if (startPos != null && startPos >= 0) {
						addHeader("RANGE", "bytes=$startPos-$endPos")
					}
				}
				.build()
		requestCall = okHttpClient.newCall(request)
		requestCall?.enqueue(this)
	}

	override fun onFailure(call: Call, e: IOException) {
		println("下载出错")
		downloadStatus = DownloadStatus.DOWNLOAD_ERROR
		downloadStatusListener.downloadStatusChage(downloadStatus)
	}

	override fun onResponse(call: Call, response: Response) {
		val body = response.body
		val randomAccessFile = RandomAccessFile(saveFile, "rwd")
		randomAccessFile.seek(currentPos)

		val byteArray = ByteArray(1024 * 1024)

		var len: Int
		body?.byteStream()?.use { inputStream ->
			while (inputStream.read(byteArray).also { len = it } != -1) {
				if (downloadStatus != DownloadStatus.DOWNLOAD_PAUSE) {
					randomAccessFile.write(byteArray, 0, len)
					downloadStatus = DownloadStatus.DOWNLOADING
					currentPos += len

				}
				downloadStatusListener.downloadStatusChage(downloadStatus)
			}
		}
		downloadStatus = DownloadStatus.DOWNLOAD_COMPLETE
		downloadStatusListener.downloadStatusChage(downloadStatus)
	}

	fun downLoadPause() {
		downloadStatus = DownloadStatus.DOWNLOAD_PAUSE
	}

	fun downloadCancel() {
		if (requestCall?.isCanceled() != true) {
			requestCall?.cancel()
			downloadStatus = DownloadStatus.DOWNLOAD_CANCEL
			downloadStatusListener.downloadStatusChage(downloadStatus)
		}
	}
}