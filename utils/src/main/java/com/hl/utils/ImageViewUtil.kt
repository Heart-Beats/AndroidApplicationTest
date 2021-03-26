package com.hl.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * @Author  张磊  on  2020/09/25 at 19:26
 * Email: 913305160@qq.com
 */

/**
 * 获取视频的首帧图
 * @param url
 */
fun ImageView.loadFirstFrameCover(url: String?, errorResIs: Int? = null, placeholderResId: Int? = null) {
	//可以参考Glide，内部也是封装了MediaMetadataRetriever
	Glide.with(this.context)
			.setDefaultRequestOptions(
					RequestOptions().apply {
						frame(1000000)
						// centerCrop()
						// error(errorResIs)
						// placeholder(placeholderResId)
					}
			)
			.load(url)
			.into(this)
}