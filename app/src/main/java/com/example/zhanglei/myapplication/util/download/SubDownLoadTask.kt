package com.example.zhanglei.myapplication.util.download

import okhttp3.*
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

/**
 * @Author  张磊  on  2020/11/04 at 15:06
 * Email: 913305160@qq.com
 */
data class SubDownLoadTask(
		val downLoadUrl: String,
		val startPos: Long,
		val endPos: Long,
		var currentPos: Long,
		var downloadStatus: DownloadStatus = DownloadStatus.NOT_DOWNLOADED,
		val saveFile: File,
		val downloadStatusListener: OnDownloadStatusListener
) : Callback {

	private val okHttpClient = OkHttpClient()

	fun startDownLoad() {
		val request = Request.Builder()
				.url(downLoadUrl)
				.addHeader("RANGE", "bytes=$startPos-$endPos")
				.build()
		okHttpClient.newCall(request).enqueue(this)
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
		body?.byteStream()?.use {
			while (it.read(byteArray).also { len = it } != -1) {
				randomAccessFile.write(byteArray, 0, len)
				println("正在下载")
				downloadStatus = DownloadStatus.DOWNLOADING
				currentPos += len
				downloadStatusListener.downloadStatusChage(downloadStatus)
			}
		}
		downloadStatus = DownloadStatus.DOWNLOAD_COMPLETE
		downloadStatusListener.downloadStatusChage(downloadStatus)
	}
}