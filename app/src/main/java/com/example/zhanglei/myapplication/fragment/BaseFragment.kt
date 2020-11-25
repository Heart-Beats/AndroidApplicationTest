package com.example.zhanglei.myapplication.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.zhanglei.myapplication.util.traverseFindFirstChildView

/**
 * @Author  张磊  on  2020/08/28 at 18:35
 * Email: 913305160@qq.com
 */
abstract class BaseFragment : Fragment() {

	companion object {
		private const val TAG = "BaseFragment"
	}

	protected abstract val layoutResId: Int

	@JvmField
	protected var toolbar: Toolbar? = null

	override fun onAttach(context: Context) {
		super.onAttach(context)
		Log.d(TAG, "onAttach =====> $this")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.d(TAG, "onCreate =====> $this")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		Log.d(TAG, "onCreateView =====> $this")

		val inflateView = inflater.inflate(layoutResId, container, false)
		toolbar = inflateView.traverseFindFirstChildView(Toolbar::class.java)

		toolbar?.apply {
			this.setTitleTextColor(Color.WHITE)
		}
		return inflateView
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.d(TAG, "onViewCreated =====> $this")
		val activity = activity
		val callback = onBackPressed()
		if (activity != null && callback != null) {
			activity.onBackPressedDispatcher.addCallback(owner = viewLifecycleOwner, onBackPressed = callback)
		}
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		Log.d(TAG, "onActivityCreated =====> $this")

		requireActivity().onBackPressedDispatcher.addCallback(owner = viewLifecycleOwner, onBackPressed = onBackPressed())

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

	override fun onStart() {
		super.onStart()
		Log.d(TAG, "onStart =====> $this")
	}

	override fun onResume() {
		super.onResume()
		Log.d(TAG, "onResume =====> $this")
	}

	override fun onPause() {
		super.onPause()
		Log.d(TAG, "onPause =====> $this")
	}

	override fun onStop() {
		super.onStop()
		Log.d(TAG, "onStop =====> $this")
	}

	override fun onDestroyView() {
		super.onDestroyView()
		Log.d(TAG, "onDestroyView =====> $this")
	}

	override fun onDestroy() {
		super.onDestroy()
		Log.d(TAG, "onDestroy =====> $this")
	}

	override fun onDetach() {
		super.onDetach()
		Log.d(TAG, "onDetach =====> $this")
	}

	protected open fun onBackPressed(): OnBackPressedCallback.() -> Unit {
		return {
			try {
				findNavController().popBackStack()
			} catch (e: Exception) {
				activity?.onBackPressed()
			}
		}
	}

}