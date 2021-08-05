package com.hl.nativelib

/**
 * @author  张磊  on  2021/08/02 at 14:44
 * Email: 913305160@qq.com
 */
class TestJNI {

	external fun test():String

	companion object {
		// Used to load the 'nativelib' library on application startup.
		init {
			System.loadLibrary("nativelib")
		}
	}
}