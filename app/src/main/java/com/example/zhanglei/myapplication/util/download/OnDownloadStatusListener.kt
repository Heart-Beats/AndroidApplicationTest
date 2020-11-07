package com.example.zhanglei.myapplication.util.download

/**
 * @Author  张磊  on  2020/11/04 at 21:22
 * Email: 913305160@qq.com
 */
internal interface OnDownloadStatusListener {

	fun downloadStatusChange(status: DownloadStatus)
}