buildscript {
	// rootProject.extra 定义的扩展属性可被子工程引用
	val kotlinVersion by extra("1.5.20")
	val hiltVersion by extra("2.37")
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
		classpath("com.android.tools.build:gradle:4.2.2")
		classpath(kotlin("gradle-plugin", kotlinVersion))

		val navVersion = "2.3.3"
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
		classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
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