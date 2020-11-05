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

	internal fun downloadStatusChange(downloadStatus: DownloadStatus, progress: Int = 0) {
		when (downloadStatus) {
			DownloadStatus.DOWNLOAD_ERROR -> downloadListener?.downloadError()
			DownloadStatus.DOWNLOADING -> downloadListener?.downloadIng(progress)
			DownloadStatus.DOWNLOAD_COMPLETE -> downloadListener?.downloadComplete()
			DownloadStatus.DOWNLOAD_PAUSE -> downloadListener?.downloadPause()
			DownloadStatus.DOWNLOAD_CANCEL -> downloadListener?.downloadCancel()
			else -> {
			}
		}
	}
}