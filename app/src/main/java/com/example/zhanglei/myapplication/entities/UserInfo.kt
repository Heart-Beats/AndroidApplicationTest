package com.example.zhanglei.myapplication.entities


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Fts4
import kotlinx.parcelize.Parcelize


/**
 * @Author  张磊  on  2021/04/01 at 18:51
 * Email: 913305160@qq.com
 */
@Fts4
@Entity(tableName = "users", ignoredColumns = arrayOf("userId"))
@Parcelize
data class UserInfo(
        var userId: String
) : Parcelable