// buildscript 不能从其他 gradle 文件中 apply，所以这段 buildscript 脚本存在于多个子构建中
buildscript {
	repositories {
		mavenCentral() // maven 中心远程仓库
		google()
		maven { url = uri("https://maven.aliyun.com/repository/central") }
		maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
		maven { url = uri("https://maven.aliyun.com/repository/google") }
		maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

	}
	dependencies {
		classpath("com.android.tools.build:gradle:7.0.2")
	}
}

task("clean", Delete::class) {
	gradle.includedBuilds.forEach {
		dependsOn(it.task(":clean"))
	}
}