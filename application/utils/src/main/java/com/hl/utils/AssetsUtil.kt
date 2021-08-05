package com.hl.utils

import android.content.Context
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * @author  张磊  on  2021/07/12 at 10:40
 * Email: 913305160@qq.com
 */

fun Context.copyAssets2Path(assetsFilePath: String, savePath: String, isCovered: Boolean): String {
	val saveFile = File(savePath)
	if (saveFile.exists() && !isCovered) {
		// 文件存在且不允许覆盖
		return saveFile.absolutePath
	} else {
		with(saveFile.parentFile) {
			if (this?.exists() == false) {
				this.mkdirs()
			}
		}
		if (!saveFile.createNewFile()) {
			println("$saveFile 已存在，覆盖原文件")
		}
	}

	val assetsInputStream = BufferedInputStream(this.assets.open(assetsFilePath))
	val bufferedOutputStream = BufferedOutputStream(FileOutputStream(saveFile))
	assetsInputStream.use { bis ->
		val byteArray = ByteArray(1024)
		var length: Int
		while (bis.read(byteArray).also { length = it } != -1) {
			bufferedOutputStream.write(byteArray, 0, length)
		}

		// 缓冲输出流必须要刷新缓存区，否则可能产生异常
		bufferedOutputStream.flush()
		bufferedOutputStream.close()
	}
	return saveFile.absolutePath
}

