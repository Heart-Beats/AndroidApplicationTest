package com.example.zhanglei.myapplication.util.download

/**
 * @Author  张磊  on  2020/11/04 at 20:45
 * Email: 913305160@qq.com
 */
object DownloadManager {

	private var downloadListener: DownloadListener? = null

	fun startDownLoad(downloadUrl: String, maxDownloadCore: Int = 1, downloadListener: DownloadListener) {
		DownloadTask(downloadUrl, maxDownloadCore).startDownload()
		this.downloadListener = downloadListener
	}

	fun downloadError() {
		downloadListener?.downloadError()
	}

	fun downloadComplete() {
		downloadListener?.downloadComplete()
	}

	fun downloadIng(progress: Long) {
		downloadListener?.downloadIng(progress)
	}
}