package com.example.zhanglei.myapplication.util

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.zhanglei.myapplication.MyApplication


/**
 * @Author  张磊  on  2020/10/29 at 10:55
 * Email: 913305160@qq.com
 */
object NotificationUtils {

    private const val MANAGE_ID = 0

    private var manager: NotificationManagerCompat? = null

    private fun getManager(): NotificationManagerCompat? {
        if (manager == null) {

            manager = NotificationManagerCompat.from(MyApplication.getInstance())
        }
        return manager
    }


    private fun createNotificationChannel(channelId: String, notificationChannel: NotificationChannel.() -> Unit) {
        //大于8.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, MyApplication.getInstance().packageName,
                    NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true) //闪光
            channel.lockscreenVisibility = Notification.VISIBILITY_SECRET //锁屏不显示通知
            channel.enableVibration(false) //是否允许震动
            channel.setBypassDnd(true) //设置可以绕过，请勿打扰模式
            channel.vibrationPattern = longArrayOf(100, 100, 200) //震动的模式，震3次，第一次100，第二次100，第三次200毫秒
            channel.apply(notificationChannel)

            //通知管理者创建的渠道
            getManager()?.createNotificationChannel(channel)
        }
    }


    fun showNotification(@DrawableRes icon: Int, title: String, content: String, channelId: String,
                         notificationBuilder: NotificationCompat.Builder.() -> Unit = {},
                         notificationChannel: NotificationChannel.() -> Unit = {}) {
        createNotificationChannel(channelId, notificationChannel)
        val builder = NotificationCompat.Builder(MyApplication.getInstance(), channelId)
                .setOnlyAlertOnce(true)
                .setDefaults(Notification.DEFAULT_ALL) //设置默认是否震动、提示音、以及闪光灯
                .setWhen(System.currentTimeMillis())
                //在用户点按通知后自动移除通知
                .setAutoCancel(true)
                .setChannelId(channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(icon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .apply(notificationBuilder)
        getManager()?.notify(MANAGE_ID, builder.build())
    }


    fun cancleNotification() {
        getManager()?.cancel(MANAGE_ID)
    }
}