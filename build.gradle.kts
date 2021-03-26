// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

	// rootProject.extra 定义的扩展属性可被子工程引用
	val kotlin_version by rootProject.extra("1.4.31")

	rootProject.extra

	repositories {
		google()
		// jcenter()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

		maven { url = uri("https://jitpack.io") }
	}
	dependencies {
		classpath("com.android.tools.build:gradle:4.1.3")
		classpath(kotlin("gradle-plugin", rootProject.extra.get("kotlin_version") as String))

		val nav_version = "2.3.3"
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
	}
}

allprojects {
	repositories {
		mavenCentral()
		google()
		jcenter()
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