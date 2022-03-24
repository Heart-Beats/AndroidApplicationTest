package com.hl.api.interceptor

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import java.io.IOException


/**
 * @author  张磊  on  2021/11/02 at 18:39
 * Email: 913305160@qq.com
 */
class RequestHeaderOrParamsInterceptor private constructor() : Interceptor {

	companion object {
		private fun bodyToString(request: RequestBody): String {
			return try {
				val buffer = Buffer()
				request.writeTo(buffer)
				buffer.readUtf8()
			} catch (e: IOException) {
				"did not work"
			}
		}
	}

	val queryParamsMap: MutableMap<String, String> = HashMap()    // 添加到 URL 末尾，Get Post 方法都使用
	val paramsMap: MutableMap<String, String> = HashMap()       // 添加到公共参数到消息体，适用 Post 请求
	val headerParamsMap: MutableMap<String, String> = HashMap()   // 公共 Headers 添加
	val headerLinesList: MutableList<String> = ArrayList()        // 消息头 集合形式，一次添加一行


	@Throws(IOException::class)
	override fun intercept(chain: Interceptor.Chain): Response {
		var request: Request = chain.request()
		val requestBuilder = request.newBuilder()

		// process header params inject
		val headerBuilder = request.headers.newBuilder()
		// 以 Entry 添加消息头
		if (headerParamsMap.isNotEmpty()) {
			val iterator: Iterator<Map.Entry<String, String>> = headerParamsMap.entries.iterator()
			while (iterator.hasNext()) {
				val entry = iterator.next()
				headerBuilder.add(entry.key, entry.value)
			}
		}
		// 以 String 形式添加消息头
		if (headerLinesList.size > 0) {
			for (line in headerLinesList) {
				headerBuilder.add(line)
			}
			requestBuilder.headers(headerBuilder.build())
		}
		// process header params end


		// process queryParams inject whatever it's GET or POST
		if (queryParamsMap.isNotEmpty()) {
			request = injectParamsIntoUrl(request.url.newBuilder(), requestBuilder, queryParamsMap)
		}

		// process post body inject
		if (paramsMap.isNotEmpty()) {
			if (canInjectIntoBody(request)) {
				val formBodyBuilder = FormBody.Builder()
				for ((key, value) in paramsMap) {
					formBodyBuilder.add(key, value)
				}
				val formBody: RequestBody = formBodyBuilder.build()
				var postBodyString = request.body?.run { bodyToString(this) } ?: ""

				postBodyString += (if (postBodyString.isNotEmpty()) "&" else "") + bodyToString(formBody)
				requestBuilder.post(
					postBodyString
						.toRequestBody("application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull())
				)
			}
		}
		request = requestBuilder.build()
		return chain.proceed(request)
	}

	/**
	 * 确认是否是 post 请求
	 * @param request 发出的请求
	 * @return true 需要注入公共参数
	 */
	private fun canInjectIntoBody(request: Request?): Boolean {
		if (request == null) {
			return false
		}
		if (request.method != "POST") {
			return false
		}
		val body = request.body ?: return false
		val mediaType = body.contentType() ?: return false
		return mediaType.subtype == "x-www-form-urlencoded"
	}

	// func to inject params into url
	private fun injectParamsIntoUrl(
		httpUrlBuilder: HttpUrl.Builder,
		requestBuilder: Request.Builder,
		paramsMap: Map<String, String>
	): Request {
		val iterator: Iterator<Map.Entry<String, String>> = paramsMap.entries.iterator()
		while (iterator.hasNext()) {
			val entry = iterator.next()
			httpUrlBuilder.addQueryParameter(entry.key, entry.value)
		}
		requestBuilder.url(httpUrlBuilder.build())
		return requestBuilder.build()
	}

	class Builder(private val interceptor: RequestHeaderOrParamsInterceptor = RequestHeaderOrParamsInterceptor()) {

		// 添加公共参数到 post 消息体
		fun addParam(key: String, value: String): Builder {
			interceptor.paramsMap[key] = value
			return this
		}

		// 添加公共参数到 post 消息体
		fun addParamsMap(paramsMap: Map<String, String>): Builder {
			interceptor.paramsMap.putAll(paramsMap!!)
			return this
		}

		// 添加公共参数到消息头
		fun addHeaderParam(key: String, value: String): Builder {
			interceptor.headerParamsMap[key] = value
			return this
		}

		// 添加公共参数到消息头
		fun addHeaderParamsMap(headerParamsMap: Map<String, String>?): Builder {
			interceptor.headerParamsMap.putAll(headerParamsMap!!)
			return this
		}

		// 添加公共参数到消息头
		fun addHeaderLine(headerLine: String): Builder {
			val index = headerLine.indexOf(":")
			require(index != -1) { "Unexpected header: $headerLine" }
			interceptor.headerLinesList.add(headerLine)
			return this
		}

		// 添加公共参数到消息头
		fun addHeaderLinesList(headerLinesList: List<String>): Builder {
			for (headerLine in headerLinesList) {
				val index = headerLine.indexOf(":")
				require(index != -1) { "Unexpected header: $headerLine" }
				interceptor.headerLinesList.add(headerLine)
			}
			return this
		}

		// 添加公共参数到 URL
		fun addQueryParam(key: String, value: String): Builder {
			interceptor.queryParamsMap[key] = value
			return this
		}

		// 添加公共参数到 URL
		fun addQueryParamsMap(queryParamsMap: Map<String, String>?): Builder {
			interceptor.queryParamsMap.putAll(queryParamsMap!!)
			return this
		}

		fun build(): RequestHeaderOrParamsInterceptor {
			return interceptor
		}
	}
}
