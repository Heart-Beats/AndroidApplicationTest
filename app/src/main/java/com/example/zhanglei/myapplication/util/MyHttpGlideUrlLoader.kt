package com.example.zhanglei.myapplication.util

import android.util.Log
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelCache
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader
import java.io.InputStream
import java.net.URL

/**
 * 定义 Glide 的 model解析
 *
 * @Author  张磊  on  2020/08/27 at 13:37
 * Email: 913305160@qq.com
 */
class MyHttpGlideUrlLoader(private var modelCache: ModelCache<MyGlideUrl, MyGlideUrl>) : HttpGlideUrlLoader() {


    private val TAG = "MyHttpGlideUrlLoader"


    inner class MyGlideUrl(private val url: URL?) : GlideUrl(url) {

        override fun getCacheKey(): String {
            Log.d(TAG, "getCacheKey: 请求图片地址== $url")

            return /*"自定义的key"*/ super.getCacheKey()
        }
    }

    override fun buildLoadData(model: GlideUrl, width: Int, height: Int, options: Options): LoadData<InputStream>? {
        Log.d(TAG, "buildLoadData: ")

        val myModel = MyGlideUrl(model.toURL())

        var url: GlideUrl? = modelCache[myModel, 0, 0]
        if (url == null) {
            modelCache.put(myModel, 0, 0, myModel)
            url = myModel
        }

        val timeout = options.get(TIMEOUT) ?: 2500
        return LoadData(url, HttpUrlFetcher(url, timeout))
    }


    class Factory : HttpGlideUrlLoader.Factory() {

        private val modelCache = ModelCache<MyGlideUrl, MyGlideUrl>(500)

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> {
            return MyHttpGlideUrlLoader(modelCache)
        }
    }
}