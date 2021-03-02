package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zhanglei.myapplication.databinding.FragmentHookBinding
import com.example.zhanglei.myapplication.fragments.base.ViewBindingBaseFragment
import com.mdit.library.proxy.Enhancer
import de.robv.android.xposed.DexposedBridge
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Proxy


class HookFragment : ViewBindingBaseFragment<FragmentHookBinding>() {

	override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentHookBinding {
		return FragmentHookBinding.inflate(inflater, container, false)
	}

	override fun FragmentHookBinding.onViewCreated(savedInstanceState: Bundle?) {
		val textModifyText = TextModifyText()

		originFunction.setOnClickListener {
			val modify = textModifyText.modify("我是原始修改的 Text 内容")
			displayContent.text = modify
		}

		dynamicProxyFunction.setOnClickListener {
			//动态代理必须将被代理的对象给替换掉，才会一直生效，为对象层面
			val textModify = Proxy.newProxyInstance(textModifyText::class.java.classLoader, textModifyText::class.java.interfaces) { _, method, args ->
				println("开始代理")
				val obj = method?.invoke(textModifyText, *args) // 这里需要注意传入的 obj 应该为被代理的对象，这样就会执行被代理对象的原始方法
				println("动态代理：代理结束, 方法调用原始值 == $obj")
				"我是动态代理后修改的 Text 内容"   //此处可以修改方法执行的返回值
			} as TextModify

			displayContent.text = textModify.modify("我是原始修改的 Text 内容")
		}

		epicHookFunction.setOnClickListener {
			//hook 方法，添加拦截处理后一直生效，为方法层面处理
			DexposedBridge.findAndHookMethod(TextModifyText::class.java, "modify2", String::class.java, object : XC_MethodHook() {
				/**
				 * Hook方法后，后续方法的执行都会被拦截处理
				 * */
				override fun afterHookedMethod(param: MethodHookParam?) {
					super.afterHookedMethod(param)
					println("afterHookedMethod：原始方法调用结束, 方法调用原始值==${param?.result}")
					param?.result = "我是 epic hook 方法后修改的 Text 内容"
				}
			})
			displayContent.text = textModifyText.modify2("我是原始修改的 Text 内容")
		}

		InterceptMethodFunction.setOnClickListener {
			/*
			* 底层为 CGLIB 实现的 类代理，与 Java 的动态代理一样，都需要替换掉被代理的对象，也为对象层面
			* 其中需要注意的是该方式有限制：必须保证被代理的类和它的所有方法都可被继承才行，否则运行即会报错，
			*    因为 kotlin 默认不可继承，尤其需要注意(被 private 和 internal 修饰运行不会报错但无效，被 protected 修饰运行会出错，被 open 修饰运行无错也有效 )
			* */

			val enhancer = Enhancer(requireContext())
			enhancer.setSuperclass(TextModifyText::class.java)
			enhancer.setCallback { `object`, args, methodProxy ->
				println("intercept  -- 拦截之前--- 方法名==${methodProxy.methodName}")
				val obj: Any = methodProxy.invokeSuper(`object`, args)
				println("intercept  -- 拦截之后 --- , 方法调用原始值 == $obj")
				"我是 MethodInterceptProxy 方法拦截代理后修改的 Text 内容"
			}

			// 测试好像无效
			// enhancer.setCallbackFilter {
			// 	1  // 方法拦截器 1--> 拦截代理， 0-->不拦截代理
			// }

			val textModifyText1 = enhancer.create() as TextModifyText
			// display_content.text = textModifyText1.modify("我是原始修改的 Text 内容")
			displayContent.text = textModifyText1.modify2("我是原始修改的 Text 内容")  // 可代理非接口中的方法
			// display_content.text = textModifyText1.modify3("我是原始修改的 Text 内容")   //modify3 方法不可继承，无法进行代理
		}
	}

}


interface TextModify {

	fun modify(value: String): String
}

open class TextModifyText : TextModify {

	override fun modify(value: String): String {
		return value
	}

	open fun modify2(value: String): String {
		return value
	}

	/**
	 *  modify3 方法不可被继承，因此无法使用 MethodInterceptProxy 进行代理
	 * */
	internal fun modify3(value: String): String {
		return value
	}
}