package com.example.zhanglei.myapplication.hilt

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntKey
import dagger.multibindings.IntoMap
import dagger.multibindings.IntoSet
import dagger.multibindings.Multibinds
import javax.inject.Inject


/**
 * @author  张磊  on  2021/07/16 at 11:02
 * Email: 913305160@qq.com
 */

interface IMultiBindings {

	fun printBindName(): String
}

class AIMultiBindingsImpl @Inject constructor() : IMultiBindings {

	override fun printBindName(): String = this.javaClass.simpleName

}

class BIMultiBindingsImpl @Inject constructor() : IMultiBindings {

	override fun printBindName(): String = this.javaClass.simpleName

}


@InstallIn(SingletonComponent::class)
@Module
abstract class IMultiBindingsBindsModule {

	@IntoMap
	@IntKey(0)
	@Binds
	abstract fun bindAIMultiBindings(impl: AIMultiBindingsImpl): IMultiBindings

	@IntoMap
	@IntKey(1)
	@Binds
	abstract fun bindBIMultiBindings(impl: BIMultiBindingsImpl): IMultiBindings
}

@InstallIn(SingletonComponent::class)
@Module
object IMultiBindingsProvidesModule {

	@Provides
	fun provideIMultiBindingsResolutionSupport(available: Map<Int, @JvmSuppressWildcards IMultiBindings>): IMultiBindings {
		val maxIndex = available.keys.maxOfOrNull { it } ?: 0
		return available.getValue(maxIndex)
	}
}


/*
*
* =====================================================================================================
*
*   set 类型的多重绑定
*
* */

@InstallIn(SingletonComponent::class)
@Module
object SetProvidesModule {

	@IntoSet
	@Provides
	fun provideSet(): AIMultiBindingsImpl {
		return AIMultiBindingsImpl()
	}

	@IntoSet
	@Provides
	fun provideSet2(): AIMultiBindingsImpl {
		return AIMultiBindingsImpl()
	}

	@IntoSet
	@Provides
	fun provideSet3(): BIMultiBindingsImpl {
		return BIMultiBindingsImpl()
	}

	@Provides
	fun provideResolutionSupport(available: Set<AIMultiBindingsImpl>): List<AIMultiBindingsImpl> {
		return available.toList()
	}

	@IntoMap
	@IntKey(0)
	@Provides
	fun provideMapMultibindsSupport(): String {
		return "1"
	}
}


@InstallIn(SingletonComponent::class)
@Module
abstract class Test{
	@Multibinds
	abstract fun testMultibinds(): Map<Int, String>
}


/*
*
* ===============================================================================================
*
*   自定义 MAP 多重绑定的 key
*
*
* */


enum class MyEnum {
	ABC, DEF
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class MyEnumKey(val value: MyEnum)

// @MustBeDocumented
// @Target(AnnotationTarget.FUNCTION)
// @Retention(AnnotationRetention.RUNTIME)
// @MapKey
// annotation class MyKey(
// 	val name: String,
// 	val implementingClass: KClass<*>,
// 	val thresholds: IntArray
// )