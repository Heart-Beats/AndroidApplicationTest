// buildscript 不能从其他 gradle 文件中 apply，所以这段 buildscript 脚本存在于多个子构建中
buildscript {
	// rootProject.extra 定义的扩展属性可被子工程引用
	val kotlinVersion by extra("1.5.21")
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
	dependencies {
		classpath("com.android.tools.build:gradle:7.0.2")
		classpath(kotlin("gradle-plugin", kotlinVersion))
	}
}

apply {
	from("../common_build.gradle.kts")
}

allprojects {
	//给所有的子模块添加组
	group = "com.hl.shadow"
}
