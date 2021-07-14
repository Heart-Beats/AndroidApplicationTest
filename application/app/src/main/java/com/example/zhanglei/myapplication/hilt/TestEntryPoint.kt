package com.example.zhanglei.myapplication.hilt

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Qualifier

/**
 * @author  张磊  on  2021/07/14 at 13:53
 * Email: 913305160@qq.com
 */

@InstallIn(SingletonComponent::class)
@EntryPoint
interface TestEntryPoint {

	val entryPointName: String

	@Provide
	fun getEntryPoint(): TestEntryPoint
}

class TestAEntryPoint @Inject constructor() : TestEntryPoint {

	override val entryPointName: String
		get() = "TestAEntryPoint"

	override fun getEntryPoint(): TestEntryPoint {
		return this
	}
}

class TestBEntryPoint @Inject constructor() : TestEntryPoint {

	override val entryPointName: String
		get() = "TestBEntryPoint"

	override fun getEntryPoint(): TestEntryPoint {
		return this
	}
}


@InstallIn(SingletonComponent::class)
@Module
abstract class EntryPointModule {

	@Bind
	@Binds
	abstract fun getTestAEntryPoint(entryPoint: TestAEntryPoint): TestEntryPoint
}

@InstallIn(SingletonComponent::class)
@Module
object ProvideEntryPointModule {

	@Provides
	fun provideEntryPointName(): String {
		return TestBEntryPoint().entryPointName
	}

	@Provide
	@Provides
	fun provideTestBEntryPoint(): TestEntryPoint {
		return TestBEntryPoint()
	}

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Bind

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Provide