package com.example.zhanglei.myapplication.util.download

/**
 * @Author  张磊  on  2020/11/04 at 15:33
 * Email: 913305160@qq.com
 */
enum class DownloadStatus(statusDesc: String) {
    NOT_DOWNLOADED("未下载"),
    DOWNLOADING("下载中"),
    DOWNLOAD_COMPLETE("下载完成"),
    DOWNLOAD_ERROR("下载错误")
}