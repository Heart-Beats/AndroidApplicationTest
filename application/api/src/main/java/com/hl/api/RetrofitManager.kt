package com.hl.api

import com.hl.api.logger.HttpLogger
import com.hl.api.interceptor.RequestHeaderOrParamsInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author  张磊  on  2021/11/02 at 16:27
 * Email: 913305160@qq.com
 */
object RetrofitManager {

	private val okHttpClientBuilder = OkHttpClient.Builder()

	var apiInterFace: Any? = null

	/**
	 * 根据泛型参数创建相应的请求接口
	 */
	inline fun <reified T> buildRetrofit(
		baseUrl: String,
		isPrintLog: Boolean = true,
		noinline publicHeaderOrParamsBlock: RequestHeaderOrParamsInterceptor.Builder.() -> Unit = {},
		okHttpBuilderBlock: OkHttpClient.Builder.() -> Unit = {},
	): T {
		return if (apiInterFace == null || apiInterFace !is T) {
			Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(GsonConverterFactory.create())
				.client(initClientBuilder(isPrintLog, publicHeaderOrParamsBlock).apply(okHttpBuilderBlock).build())
				.build()
				.create(T::class.java).also {
					apiInterFace = it
				}
		} else apiInterFace as T
	}


	fun initClientBuilder(
		isPrintLog: Boolean,
		publicHeaderOrParamsBlock: RequestHeaderOrParamsInterceptor.Builder.() -> Unit = {}
	): OkHttpClient.Builder {
		return okHttpClientBuilder
			.connectTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.writeTimeout(60, TimeUnit.SECONDS)
			.sslSocketFactory(getSSLSocketFactory(), createX509TrustManager())
			.hostnameVerifier(getHostnameVerifier())
			//错误重连
			.retryOnConnectionFailure(true).apply {
				if (isPrintLog) {
					//添加retrofit log拦截器, 打印日志
					addInterceptor(HttpLoggingInterceptor(HttpLogger()))
				}

				val headerBuilder: RequestHeaderOrParamsInterceptor.Builder = RequestHeaderOrParamsInterceptor.Builder()
					.addHeaderParam("Content-Type", "application/json")

				addInterceptor(headerBuilder.apply(publicHeaderOrParamsBlock).build())
				// addInterceptor(AddCookiesInterceptor())
				// addInterceptor(ReceivedCookiesInterceptor())
			}
	}

	//获取TrustManager
	private fun createX509TrustManager(): X509TrustManager {
		//不校检证书链
		return object : X509TrustManager {
			override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
				//不校检客户端证书
			}

			override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
				//不校检服务器证书
			}

			override fun getAcceptedIssuers(): Array<X509Certificate> {
				return arrayOf()
				//OKhttp3.0以前返回 null,3.0 以后返回 new X509Certificate[]{};
			}
		}
	}


	//通过这个类可以获得 SSLSocketFactory，这个东西就是用来管理证书和信任证书的
	private fun getSSLSocketFactory(): SSLSocketFactory {
		return try {
			val sslContext = SSLContext.getInstance("SSL")
			sslContext.init(null, getTrustManagerArray(), SecureRandom())
			sslContext.socketFactory
		} catch (e: Exception) {
			throw RuntimeException(e)
		}
	}

	//获取TrustManager
	private fun getTrustManagerArray(): Array<TrustManager> {
		//不校检证书链
		return arrayOf(createX509TrustManager())
	}

	//获取Hostname Verifier
	private fun getHostnameVerifier(): HostnameVerifier {
		return HostnameVerifier { s, sslSession -> //未真正校检服务器端证书域名
			true
		}
	}
}