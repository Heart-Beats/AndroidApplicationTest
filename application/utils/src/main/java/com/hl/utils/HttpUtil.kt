package com.hl.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author  张磊  on  2021/11/05 at 15:02
 * Email: 913305160@qq.com
 */

fun String.isUrl(): Boolean {

	val regex = ("^((https|http|ftp|rtsp|mms)?://)"
			+ "(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
			+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
			+ "|" // 允许IP和DOMAIN（域名）
			+ "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
			//                 + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
			+ "[a-z]{2,6})" // first level domain- .com or .museum
			+ "(:[0-9]{1,4})?" // 端口- :80
			+ "((/?)|" // a slash isn't required if there is no file name
			+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$")
	return Pattern.compile(regex).matcher(this).matches()
}