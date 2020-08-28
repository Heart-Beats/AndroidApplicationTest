package com.example.zhanglei.myapplication.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

/**
 * @Author  张磊  on  2020/08/28 at 18:35
 * Email: 913305160@qq.com
 */
abstract class BaseFragment : Fragment() {

	private val TAG = "BaseFragment"

	private val layoutResId: Int by lazy {
		layoutResId()
	}

	private lateinit var toolbar: Toolbar

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

		val inflateView = inflater.inflate(layoutResId, container, false)
		this.lifecycle.addObserver(object : LifecycleEventObserver {

			override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
				when (event) {
					Lifecycle.Event.ON_CREATE -> {
						toolbar = Toolbar(requireContext())
						val appCompatActivity = activity as? AppCompatActivity
						appCompatActivity?.setSupportActionBar(toolbar)
					}
					else -> {
					}
				}
			}

		})

		return inflateView
	}

	protected abstract fun layoutResId(): Int

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
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

					toolbar?.title = currentDestination.label

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

	override fun onStart() {
		super.onStart()
		val appCompatActivity = activity as? AppCompatActivity
		Log.d(TAG, "onStart: $toolbar")
		appCompatActivity?.setSupportActionBar(toolbar)
	}
}