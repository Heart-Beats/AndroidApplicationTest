package com.jkj.huilaidian.merchant.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zhanglei.myapplication.MyApplication
import com.tencent.connect.UserInfo

/**
 * @Author  张磊  on  2021/03/04 at 14:10
 * Email: 913305160@qq.com
 */

@Database(entities = [UserInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(MyApplication.getInstance(), AppDataBase::class.java, "hld_app.db")
                .allowMainThreadQueries()
                .build()
        }

        fun createDatabase(context: Context, dataBaseName: String, roomDatabaseBuilder: Builder<AppDataBase>.() -> Unit): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, dataBaseName)
                .allowMainThreadQueries()
                .apply { roomDatabaseBuilder() }
                .build()
        }
    }
}