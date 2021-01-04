package com.example.zhanglei.myapplication.utils

import com.elvishew.xlog.XLog

/**
 * @Author  张磊  on  2020/12/31 at 0:11
 * Email: 913305160@qq.com
 */
class ReflectHelper<T>(private val clazz: Class<T>) {

    fun <V> getFiledValue(obj: T, filedName: String): V? {
        return try {
            val field = clazz.getDeclaredField(filedName)
            field.isAccessible = true
            val get = field.get(obj)
            field.isAccessible = false
            get as? V
        } catch (e: Exception) {
            XLog.e(e)
            null
        }
    }

    fun setFiledValue(obj: T, filedName: String, value: Any?) {
        try {
            val field = clazz.getDeclaredField(filedName)
            field.isAccessible = true
            field.set(obj, value)
            field.isAccessible = false
        } catch (e: Exception) {
            XLog.e(e)
        }
    }

}