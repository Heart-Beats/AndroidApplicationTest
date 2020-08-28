plugins {
	this.id("com.android.application")
}

apply {
	plugin("com.android.application")
	plugin("kotlin-android")
	plugin("kotlin-android-extensions")
}

android {
	compileSdkVersion(30)
	defaultConfig {
		applicationId = "com.example.zhanglei.myapplication"
		minSdkVersion(15)
		targetSdkVersion(30)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
	}
	buildToolsVersion = "28.0.3"
}

android.applicationVariants.all { variant ->

	val buildType = variant.buildType.name

	//获取当前时间的"YYYY-MM-dd"格式。
	// val createTime = new Date().format("YYYY-MM-dd", java.util.TimeZone.getTimeZone("GMT+08:00"))
	println(variant.packageApplicationProvider?.get()?.outputDirectory)

	// def output = variant.outputFile()

	variant.outputs.forEach {
		if (buildType == "release" || buildType == "debug") {
			//variant.getPackageApplicationProvider().get().outputDirectory = new File("/Volumes/Morley/User/张磊")
			val fileName = "XX_${buildType}.apk"
			println("文件名：-----------------${fileName}")

			it.outputFile.renameTo(File("${it.outputFile.path}/fileName"))
		}
	}
	true
}

dependencies {
	implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.constraintlayout:constraintlayout:2.0.0")
	implementation("androidx.recyclerview:recyclerview:1.1.0")
	implementation("androidx.cardview:cardview:1.0.0")
	implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
	implementation("androidx.navigation:navigation-ui-ktx:2.3.0")


    //图片选择库
	implementation("com.zhihu.android:matisse:0.5.3-beta3")

	implementation("com.github.bumptech.glide:glide:4.11.0")
	//处理Glide注解，用于自定义Glide模块和自定义扩展类时必须引用
    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

	annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
	testImplementation("junit:junit:4.13")
	androidTestImplementation("androidx.test.ext:junit:1.1.2")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
	implementation("androidx.annotation:annotation:1.1.0")
	implementation("org.mockito:mockito-all:2.0.2-beta")
	implementation("androidx.core:core-ktx:1.3.1")

	val kotlin_version = "1.4.0"
	implementation(kotlin("stdlib-jdk7", kotlin_version))
}

repositories {
	mavenCentral()
}