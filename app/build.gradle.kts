plugins {
	this.id("com.android.application")
}

apply {
	plugin("kotlin-android")
	plugin("kotlin-android-extensions")
	plugin("kotlin-kapt")
}

android {
	compileSdkVersion(30)
	defaultConfig {
		applicationId = "com.example.zhanglei.myapplication"
		minSdkVersion(23)
		targetSdkVersion(30)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		// Required when setting minSdkVersion to 20 or lower（支持请求比minSdkVersion更高的API）
		multiDexEnabled = true
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
		}
	}
	buildToolsVersion = "29.0.3"

	compileOptions {
		//支持新的API(java.util.streams、java.util.function等)
		this.isCoreLibraryDesugaringEnabled = true
		this.sourceCompatibility = JavaVersion.VERSION_1_8
		this.targetCompatibility = JavaVersion.VERSION_1_8
	}

	tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
		kotlinOptions {
			this.jvmTarget = "1.8"
		}
	}
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
	api(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
	implementation("androidx.legacy:legacy-support-v4:1.0.0")

	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.10")
	testImplementation("junit:junit:4.13")
	androidTestImplementation("androidx.test.ext:junit:1.1.2")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.10")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
	implementation("androidx.annotation:annotation:1.1.0")
	implementation("org.mockito:mockito-all:2.0.2-beta")
	implementation("androidx.core:core-ktx:1.3.2")

	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.constraintlayout:constraintlayout:2.0.2")
	implementation("androidx.recyclerview:recyclerview:1.1.0")
	implementation("androidx.cardview:cardview:1.0.0")
	implementation("androidx.navigation:navigation-fragment-ktx:2.3.0")
	implementation("androidx.navigation:navigation-ui-ktx:2.3.0")

	implementation("com.google.code.gson:gson:2.8.6")

	//lottie动画库
	implementation("com.airbnb.android:lottie:3.4.2")
	implementation("com.scwang.smart:refresh-layout-kernel:2.0.1")

	implementation("com.elvishew:xlog:1.6.1")

	//图片选择库
	implementation("com.zhihu.android:matisse:0.5.3-beta3")

	implementation("com.github.bumptech.glide:glide:4.11.0")
	//处理Glide注解，用于自定义Glide模块和自定义扩展类时必须引用
	annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")

	//权限请求库
	implementation("com.permissionx.guolindev:permissionx:1.4.0")

	//视频播放库
	implementation("com.shuyu:gsyVideoPlayer-java:7.1.6")
	//是否需要ExoPlayer模式
	implementation("com.shuyu:GSYVideoPlayer-exo2:7.1.6")

	//根据你的需求ijk模式的so
	implementation("com.shuyu:gsyVideoPlayer-armv5:7.1.6")
	implementation("com.shuyu:gsyVideoPlayer-armv7a:7.1.6")
	implementation("com.shuyu:gsyVideoPlayer-arm64:7.1.6")
	implementation("com.shuyu:gsyVideoPlayer-x64:7.1.6")
	implementation("com.shuyu:gsyVideoPlayer-x86:7.1.6")

	implementation("com.squareup.okhttp3:okhttp:4.9.0")
}