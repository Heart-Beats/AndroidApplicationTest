package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zhanglei.myapplication.databinding.FragmentJniBinding
import com.example.zhanglei.myapplication.fragments.base.ViewBindingBaseFragment
import com.hl.nativelib.TestJNI

/**
 * @author  张磊  on  2021/08/02 at 16:37
 * Email: 913305160@qq.com
 */
class JNIFragment : ViewBindingBaseFragment<FragmentJniBinding>() {

	override fun createViewBinding(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): FragmentJniBinding {
		return FragmentJniBinding.inflate(inflater,container,false)
	}

	override fun FragmentJniBinding.onViewCreated(savedInstanceState: Bundle?) {
		this.displayInfo.text = TestJNI().test()
	}
}