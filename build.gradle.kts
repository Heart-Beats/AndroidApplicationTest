// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

	// rootProject.extra 定义的扩展属性可被子工程引用
	val kotlinVersion by rootProject.extra("1.5.10")

	// val group_suffix by rootProject.extra{
	// 	val needCustom = false
	// 	//定义共享属性
	// 	if(needCustom) 	".test" else ""
	// }

	repositories {
		mavenCentral()
		mavenLocal()
		google()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

		maven { url = uri("https://jitpack.io") }
	}
	dependencies {
		classpath("com.android.tools.build:gradle:4.2.1")
		classpath(kotlin("gradle-plugin", kotlinVersion))

		val navVersion = "2.3.3"
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
	}
}

allprojects {
	repositories {
		mavenCentral() // maven 中心远程仓库
		mavenLocal()  // maven 本地仓库
		google()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

		maven { url = uri("https://jitpack.io") }
	}
}

tasks.register("clean", Delete::class) {
	this.delete(rootProject.buildDir)
}