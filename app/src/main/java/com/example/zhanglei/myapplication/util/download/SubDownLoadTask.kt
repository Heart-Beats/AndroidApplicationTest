package com.example.zhanglei.myapplication.util.download

import android.telephony.mbms.DownloadStatusListener
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
		var completeSize: Long = startPos ?: 0,
		var downloadStatus: DownloadStatus = DownloadStatus.NOT_DOWNLOADED,
		val saveFile: File
) : Callback {

	private var requestCall: Call? = null

	// @Transient
	private var downloadStatusListener: OnDownloadStatusListener? = null

	fun startDownLoad(downloadListener: OnDownloadStatusListener) {
		this.downloadStatusListener = downloadListener

		//OkHttpClient该对象不可作为属性被 Gson 序列化，需注意

		val okHttpClient = OkHttpClient()
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
		downloadStatus = DownloadStatus.DOWNLOAD_ERROR
		downloadStatusListener?.downloadStatusChange(downloadStatus)
	}

	override fun onResponse(call: Call, response: Response) {
		val body = response.body
		val randomAccessFile = RandomAccessFile(saveFile, "rwd")
		//randomAccessFile.seek(pos): pos 代表要跳过的字节数，若为 0 则表示未见开头
		randomAccessFile.seek(completeSize)

		val byteArray = ByteArray(1024 * 1024)

		var len: Int
		body?.byteStream()?.use { inputStream ->
			while (inputStream.read(byteArray).also { len = it } != -1) {
				if (downloadStatus != DownloadStatus.DOWNLOAD_PAUSE) {
					randomAccessFile.write(byteArray, 0, len)
					downloadStatus = DownloadStatus.DOWNLOADING
					completeSize += len
				}
				downloadStatusListener?.downloadStatusChange(downloadStatus)
			}
		}
		downloadStatus = DownloadStatus.DOWNLOAD_COMPLETE
		downloadStatusListener?.downloadStatusChange(downloadStatus)
	}

	fun downLoadPause() {
		downloadStatus = DownloadStatus.DOWNLOAD_PAUSE
		downloadStatusListener?.downloadStatusChange(downloadStatus)
	}

	fun downloadCancel() {
		if (requestCall?.isCanceled() != true) {
			requestCall?.cancel()
			downloadStatus = DownloadStatus.DOWNLOAD_CANCEL
			downloadStatusListener?.downloadStatusChange(downloadStatus)
			downloadStatusListener = null
		}
	}
}