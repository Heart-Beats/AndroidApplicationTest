// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

	repositories {
		// google()
		// jcenter()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
	}
	dependencies {
		classpath("com.android.tools.build:gradle:4.0.1")

		val kotlin_version = "1.4.0"
		classpath(kotlin("gradle-plugin", kotlin_version))
	}
}

allprojects {
	repositories {
		// google()
		// jcenter()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
	}
}

tasks.register("clean", Delete::class) {
	this.delete(rootProject.buildDir)
}