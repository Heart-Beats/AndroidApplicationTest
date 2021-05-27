// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

	// rootProject.extra 定义的扩展属性可被子工程引用
	val kotlinVersion by rootProject.extra("1.4.31")

	repositories {
		google()
		// jcenter()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

		maven { url = uri("https://jitpack.io") }

		"E:\\repository".also {
			if (File(it).exists()) {
				maven { url = uri(it) }
			}
		}
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
		mavenCentral()
		google()
		jcenter()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

		maven { url = uri("https://jitpack.io") }

		// 加入本地仓库地址
		"E:\\repository".also {
			if (File(it).exists()) {
				maven { url = uri(it) }
			}
		}
	}
}

tasks.register("clean", Delete::class) {
	this.delete(rootProject.buildDir)
}