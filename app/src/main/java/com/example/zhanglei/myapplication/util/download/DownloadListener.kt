package com.example.zhanglei.myapplication.util.download

/**
 * @Author  张磊  on  2020/11/04 at 20:47
 * Email: 913305160@qq.com
 */
interface DownloadListener {

	fun downloadError()

	fun downloadComplete()

	fun downloadIng(progress: Long)
}