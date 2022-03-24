package com.hl.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

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

// fun View.glideLoadBankBg(context: Context?, bankId: String?, radius: Radius = Radius(context.dp(8f), context.dp(8f), context.dp(8f), context.dp(8f))) {
//     if (context == null) return
//     Glide.with(context)
//             .load("file:///android_asset/userbank_bg/bg_userbank_${bankId}.png")
//             .error(R.drawable.bg_userbank_default)
//             .placeholder(R.drawable.bg_userbank_default)
//             .into(ViewBackgroundTarget(this, radius.topLeftRadius, radius.topRightRadius, radius.bottomLeftRadius, radius.bottomRightRadius))
// }
//
// class Radius(val topLeftRadius: Float = 0f,
//              val topRightRadius: Float = 0f,
//              val bottomLeftRadius: Float = 0f,
//              val bottomRightRadius: Float = 0f)
//
// fun ImageView.glideLoadBank(context: Context?, bankId: String?, circleBg: Boolean = true): Boolean {
//     if (context == null) return false
//     val fileName = "userbank_logo/logo_userbank_${bankId}.png"
//     val success = try {
//         context.assets.openFd(fileName).close()
//         true
//     } catch (e: Exception) {
//         false
//     }
//     Glide.with(context)
//             .asBitmap()
//             .load("file:///android_asset/${fileName}")
//             .error(R.drawable.logo_userbank_defalut)
//             .into(object : BitmapImageViewTarget(this) {
//                 override fun onLoadFailed(errorDrawable: Drawable?) {
//                     if (circleBg) {
//                         getSize { width, height ->
//                             val colors = IntArray(width * height) {
//                                 Color.WHITE
//                             }
//                             val background = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888)
//                             val n = CircleCrop().transform(context, SimpleResource(background), width, height).get()
//                             view.background = BitmapDrawable(context.resources, n)
//                         }
//                     }
//                     view.setImageDrawable(errorDrawable)
//                 }
//
//                 override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
//                     if (circleBg) {
//                         getSize { width, height ->
//                             val colors = IntArray(width * height) {
//                                 Color.WHITE
//                             }
//                             val background = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888)
//                             val n = CircleCrop().transform(context, SimpleResource(background), width, height).get()
//                             view.background = BitmapDrawable(context.resources, n)
//                         }
//                     }
//                     view.setImageBitmap(bitmap)
//                 }
//             })
//     return success
// }

fun ImageView.glideLoad(context: Context?, url: String) {
	if (url.isNotEmpty() && context != null) {
		Glide.with(context)
			.load(url)
			.into(this)
	}
}

fun ImageView.glideLoad(fragment: Fragment?, url: String, holderId: Int = -1) {
	if (url.isNotEmpty() && fragment != null) {
		Glide.with(fragment)
			.load(url)
			.apply {
				if (holderId != -1) {
					placeholder(holderId)
				}
			}
			.into(this)
	}
}

fun ImageView.glideLoad(
	fragment: Fragment?,
	uri: Uri?,
	holderId: Int = -1,
	onException: (() -> Unit)? = null,
	onReady: (() -> Unit)? = null
) {
	if (fragment != null && uri != null) {
		Glide.with(fragment)
			.load(uri)
			.apply {
				if (holderId != -1) {
					placeholder(holderId)
				}
				if (onException != null || onReady != null) {
					listener(object : RequestListener<Drawable> {
						override fun onLoadFailed(
							e: GlideException?,
							model: Any?,
							target: Target<Drawable>?,
							isFirstResource: Boolean
						): Boolean {
							onException?.invoke()
							return false
						}

						override fun onResourceReady(
							resource: Drawable?,
							model: Any?,
							target: Target<Drawable>?,
							dataSource: DataSource?,
							isFirstResource: Boolean
						): Boolean {
							onReady?.invoke()
							return false
						}
					})
				}
			}
			.into(this)
	}
}

fun ImageView.glideLoadById(
	fragment: Fragment?,
	imgPath: String?,
	holderId: Int = -1,
	onException: (() -> Unit)? = null,
	onReady: (() -> Unit)? = null
) {
	if (fragment != null && !imgPath.isNullOrEmpty()) {
		Glide.with(fragment)
			.load("${imgPath}")
			.apply {
				if (holderId != -1) {
					placeholder(holderId)
				}
				if (onException != null || onReady != null) {
					listener(object : RequestListener<Drawable> {
						override fun onLoadFailed(
							e: GlideException?,
							model: Any?,
							target: Target<Drawable>?,
							isFirstResource: Boolean
						): Boolean {
							onException?.invoke()
							return false
						}

						override fun onResourceReady(
							resource: Drawable?,
							model: Any?,
							target: Target<Drawable>?,
							dataSource: DataSource?,
							isFirstResource: Boolean
						): Boolean {
							onReady?.invoke()
							return false
						}
					})
				}
			}
			.into(this)
	}
}