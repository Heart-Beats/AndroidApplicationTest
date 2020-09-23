package com.example.zhanglei.myapplication.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.zhanglei.myapplication.R

/**
 * @Author  张磊  on  2020/08/28 at 18:35
 * Email: 913305160@qq.com
 */
abstract class BaseFragment : Fragment() {

	private val TAG = "BaseFragment"

	protected abstract val layoutResId: Int

	@JvmField
	protected var toolbar: Toolbar? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val inflateView = inflater.inflate(layoutResId, container, false)

		inflateView.findViewById<View>(R.id.toolbar).run {
			if (this is Toolbar) {
				toolbar = this
			}
		}

		toolbar?.apply {
			this.setTitleTextColor(Color.WHITE)
		}
		return inflateView
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		//Activity 创建之后设置toolbar
		val appCompatActivity = activity as? AppCompatActivity
		appCompatActivity?.setSupportActionBar(toolbar)

		val currentDestination = try {
			findNavController().currentDestination
		} catch (e: Exception) {
			null
		}
		Log.d(TAG, "当前 fragment == ${this.javaClass.name},  当前标题栏 ======= ${toolbar?.title}")
		Log.d(TAG, "导航当前所在地 label =========== ${currentDestination?.label}")

		if (currentDestination is FragmentNavigator.Destination) {
			// 判断目的地为 fragment

			if (this.javaClass.name == currentDestination.className) {
				// 判断当前 fragment 是否为导航当前所在页面

				if (toolbar?.title.isNullOrEmpty()) {
					//当前页面 无 title 时的处理

					currentDestination.label = when (currentDestination.className) {
						// MainHomeFragment::class.java.name -> "首页"
						// MainMrchFragment::class.java.name -> "商户中心"
						// MainMeFragment::class.java.name -> "我的"
						else -> {
							""
						}
					}
				} else {
					//当前页面有 title
					currentDestination.label = toolbar?.title
				}
			}
		}
	}

}