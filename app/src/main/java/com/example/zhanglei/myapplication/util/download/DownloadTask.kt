package com.example.zhanglei.myapplication.util.download

/**
 * @Author  张磊  on  2020/11/04 at 15:03
 * Email: 913305160@qq.com
 */
class DownloadTask(val downloadUrl: String, val maxDownloadCore: Int = 1) {


	private val subDownLoadTasks = mutableListOf<SubDownLoadTask>()

	fun startDownload() {

	}

	private inner class DownloadStatusListener : OnDownloadStatusListener {

		override fun downloadStatusChage(status: DownloadStatus) {
			when (status) {
				DownloadStatus.DOWNLOAD_ERROR -> {

				}

				DownloadStatus.DOWNLOADING -> {
					val sum = subDownLoadTasks.fold(0L) { sum, subDownLoadTask ->
						sum + subDownLoadTask.currentPos - subDownLoadTask.startPos + 1
					}
					DownloadManager.downloadIng(sum)
				}

				DownloadStatus.DOWNLOAD_COMPLETE -> {
					if (subDownLoadTasks.all {
								it.downloadStatus == DownloadStatus.DOWNLOAD_COMPLETE
							}) {
						DownloadManager.downloadComplete()
					}
				}
			}
		}
	}

}