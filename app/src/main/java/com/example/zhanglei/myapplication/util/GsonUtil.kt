package com.example.zhanglei.myapplication.util

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @Author  张磊  on  2020/10/21 at 17:41
 * Email: 913305160@qq.com
 */

/**
 * 将字符串数组转换为 List 对象
 */
inline fun <reified T> gsonParseJson2List(json: String?): List<T>? {
    return Gson().fromJson<List<T>>(json, ParameterizedTypeImpl(T::class.java))
}

class ParameterizedTypeImpl(private val clz: Class<*>) : ParameterizedType {
    override fun getRawType(): Type = List::class.java

    override fun getOwnerType(): Type? = null

    override fun getActualTypeArguments(): Array<Type> = arrayOf(clz)
}