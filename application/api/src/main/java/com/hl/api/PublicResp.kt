package com.hl.api

/**
 * @author  张磊  on  2021/11/02 at 19:41
 * Email: 913305160@qq.com
 */

open class PublicResp<T>(
	var code: String? = null,
	var msg: String? = null,

	/**
	 * 返回的实体数据
	 */
	var data: T? = null,
) {

	fun isSucess(): Boolean {
		return this.code == "200"
	}

	/**
	 * 被踢下线
	 */
	fun offline(): Boolean {
		return "STM998" == code
	}

	override fun toString(): String {
		return "PublicResp(code=$code, msg=$msg, data=$data)"
	}
}