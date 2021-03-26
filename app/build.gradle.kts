import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
	this.id("com.android.application")
}

apply {
	plugin("kotlin-android")
	plugin("kotlin-kapt")
	plugin("kotlin-parcelize")
	plugin("androidx.navigation.safeargs.kotlin")
	from("../app_common.gradle.kts")
}

android {
	signingConfigs {
		create("release") {
			val keystoreFilePath = rootProject.projectDir.path + File.separator + "keystore.properties"
			loadProperties(keystoreFilePath).also {
				storeFile = File(it.getProperty("storeFile"))
				storePassword = it.getProperty("storePassword")
				keyAlias = it.getProperty("keyAlias")
				keyPassword = it.getProperty("keyPassword")
			}
		}
		getByName("debug") {
			storeFile = rootProject.file("debug_sign.jks")
			storePassword = "123456"
			keyAlias = "debug"
			keyPassword = "123456"
		}
	}
	compileSdkVersion(30)
	defaultConfig {
		applicationId = "com.example.zhanglei.myapplication"
		minSdkVersion(22)
		targetSdkVersion(30)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		// Required when setting minSdkVersion to 20 or lower（支持请求比minSdkVersion更高的API）
		multiDexEnabled = true

		this.setManifestPlaceholders(mapOf(
				"QQ_APPID" to "101939940",
				"SINA_REDIRECT_URI" to "",
				"SINA_SECRET" to "",
				"SINA_APPKEY" to "",
				"WX_SECRET" to "",
				"WX_APPID" to "",
				"MIUI_APPID" to "",
				"MIUI_APPSECRET" to "",
				"MIUI_REDIRECT_URI" to "",
				"BAIDU_APP_ID" to "",
				"BAIDU_API_KEY" to "",
				"BAIDU_SECRET_KEY" to ""
		))
	}
	buildTypes {
		getByName("release") {
			isMinifyEnabled = true
			isDebuggable = true
			isShrinkResources = true
			proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
			signingConfig = signingConfigs.getByName("release")
		}
		getByName("debug") {
			signingConfig = signingConfigs.getByName("debug")
		}
	}
	buildToolsVersion = "29.0.3"

	compileOptions {
		//支持新的API(java.util.streams、java.util.function等)
		this.isCoreLibraryDesugaringEnabled = true
		this.sourceCompatibility = JavaVersion.VERSION_1_8
		this.targetCompatibility = JavaVersion.VERSION_1_8
	}

	buildFeatures {
		this.viewBinding = true //启动viewBinding
	}

	flavorDimensions("MyApp")
	productFlavors {
		create("MyAppT") {
			dimension = "MyApp"
			resValue("string", "app_name", "我的应用测试")
			applicationIdSuffix = ".test"
			resConfigs()
		}
		create("MyAppP") {
			dimension = "MyApp"
			resValue("string", "app_name", "我的应用")
		}
	}

	tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
		kotlinOptions {
			this.useIR = true
			this.jvmTarget = "1.8"
		}
	}

}

android.applicationVariants.all {
	val buildType = this.buildType.name

	//获取当前时间的"YYYY-MM-dd"格式。
	// val createTime = new Date().format("YYYY-MM-dd", java.util.TimeZone.getTimeZone("GMT+08:00"))

	this.outputs.all {
		if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl)
			if (buildType == "release" || buildType == "debug") {
				//variant.getPackageApplicationProvider().get().outputDirectory = new File("/Volumes/Morley/User/张磊")
				val fileName = "myapplication-${this.versionCode}_${buildType}.apk"
				println("文件名：-----------------${fileName}")
				this.outputFileName = fileName
			}
	}
}

dependencies {
	api(fileTree("dir" to "libs", "include" to listOf("*.jar")))
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("com.google.android.material:material:1.3.0-alpha04")
	implementation(project(mapOf("path" to ":uni_sdk")))
	implementation(project(mapOf("path" to ":method-proxy-library")))
	implementation(project(mapOf("path" to ":utils")))

	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.1")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.2")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")


	implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra.get("kotlin_version")}")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
	implementation("androidx.annotation:annotation:1.1.0")
	implementation("androidx.core:core-ktx:1.3.2")
	implementation("org.jetbrains.anko:anko-commons:0.10.8")

	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
	implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

	implementation("androidx.core:core:1.3.2")
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.constraintlayout:constraintlayout:2.0.4")

	//lottie动画库
	implementation("com.airbnb.android:lottie:3.6.1")
	implementation("com.scwang.smart:refresh-layout-kernel:2.0.3")

	implementation("com.elvishew:xlog:1.8.0")

	//图片选择库
	implementation("com.zhihu.android:matisse:0.5.3-beta3")

	implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")

	implementation("com.github.Heart-Beats:Downloader:v1.0.1")

	implementation("me.weishu:epic:0.11.0")
}