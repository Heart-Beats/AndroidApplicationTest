package com.jkj.huilaidian.merchant.database

import androidx.room.*
import com.tencent.connect.UserInfo

/**
 * @Author  张磊  on  2021/03/04 at 11:18
 * Email: 913305160@qq.com
 */

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(vararg users: UserInfo): List<Long>?

    @Update
    suspend fun updateUsers(vararg users: UserInfo): Int?

    @Delete
    suspend fun deleteUsers(vararg users: UserInfo): Int?

    @Query("Delete FROM users")
    suspend fun deleteAllUsers(): Int?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserInfo>?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getCurrentUser(): UserInfo?
}