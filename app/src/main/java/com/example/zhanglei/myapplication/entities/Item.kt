package com.example.zhanglei.myapplication.entities

import androidx.annotation.DrawableRes

/**
 * @Author  张磊  on  2021/03/02 at 13:44
 * Email: 913305160@qq.com
 */

sealed class MenuItem

sealed class MainMenu(@DrawableRes val icon: Int? = null, val title: String) : MenuItem() {
    class AnimateAction : MainMenu(title = "开始动画")
    class PictureAction : MainMenu(title = "开始图片选择")
    class VideoAction : MainMenu(title = "播放视频")
    class DownLoadAction : MainMenu(title = "下载/长按取消下载")
    class NotificationAction : MainMenu(title = "创建通知")
    class HookAction : MainMenu(title = "测试Hook方法")
    class UniAppAction : MainMenu(title = "测试UniApp")
}

val mainMenuList = listOf(
        MainMenu.AnimateAction(),
        MainMenu.PictureAction(),
        MainMenu.VideoAction(),
        MainMenu.DownLoadAction(),
        MainMenu.NotificationAction(),
        MainMenu.HookAction(),
        MainMenu.UniAppAction()
)