plugins {
	id("com.android.application")
	id("kotlin-android")
	id("kotlin-kapt")
	id("kotlin-parcelize")
	id("androidx.navigation.safeargs.kotlin")
	id("dagger.hilt.android.plugin")
}

apply {
	from("../app_common.gradle.kts")
	from("../../shadow/shadow_common.gradle")
}

android {
	signingConfigs {
		create("release") {
			val keystoreFilePath = rootProject.projectDir.path + File.separator + "keystore.properties"
			if (File(keystoreFilePath).exists()) {
				org.jetbrains.kotlin.konan.properties.loadProperties(keystoreFilePath).also {
					storeFile = File(it.getProperty("storeFile"))
					storePassword = it.getProperty("storePassword")
					keyAlias = it.getProperty("keyAlias")
					keyPassword = it.getProperty("keyPassword")
				}
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
		targetSdkVersion(29)
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		// Required when setting minSdkVersion to 20 or lower（支持请求比minSdkVersion更高的API）
		multiDexEnabled = true

		setManifestPlaceholders(mapOf(
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

	compileOptions {
		//支持新的API(java.util.streams、java.util.function等)
		this.isCoreLibraryDesugaringEnabled = true
		this.sourceCompatibility = JavaVersion.VERSION_1_8
		this.targetCompatibility = JavaVersion.VERSION_1_8
	}

	buildFeatures {
		this.viewBinding = true //启动viewBinding
	}

	flavorDimensions("Environment")
	productFlavors {

		create("EnvT") { //测试环境
			dimension = "Environment"
			resValue("string", "app_name", "我的应用测试")
			applicationIdSuffix = ".test"
			resConfigs()
		}
		create("EnvP") { //生产环境
			dimension = "Environment"
			resValue("string", "app_name", "我的应用")
		}
	}

	tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
		kotlinOptions {
			this.jvmTarget = "1.8"
		}
	}
}

kapt {
	// 启用存根中的错误类型推断
	correctErrorTypes = true

	javacOptions {
		//这些选项通常通过 Hilt Gradle 插件自动设置，但这里我们需要手动设置它们以解决 Hilt 在 Kotlin 1.5.20 的构建错误
		option("-Adagger.fastInit=ENABLED")
		option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
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
	implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))
	implementation(project(mapOf("path" to ":uni-sdk")))
	implementation(project(mapOf("path" to ":method-proxy-library")))
	implementation(project(mapOf("path" to ":utils")))

	implementation("com.hl.shadow:shadow-init")
	implementation("com.hl.shadow:plugin-aidl")

	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("com.google.android.material:material:1.3.0-alpha04")
	coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.1")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.2")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

	implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.ext["kotlinVersion"]}")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.20")
	implementation("androidx.annotation:annotation:1.2.0")
	implementation("androidx.core:core-ktx:1.5.0")
	implementation("org.jetbrains.anko:anko-commons:0.10.8")

	val lifecycleVersion = "2.2.0"
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")

	implementation("androidx.core:core:1.5.0")
	implementation("androidx.appcompat:appcompat:1.3.0")
	implementation("androidx.constraintlayout:constraintlayout:2.0.4")

	//lottie动画库
	implementation("com.airbnb.android:lottie:3.7.0")
	implementation("com.scwang.smart:refresh-layout-kernel:2.0.3")

	implementation("com.elvishew:xlog:1.10.1")

	//图片选择库
	implementation("com.zhihu.android:matisse:0.5.3-beta3")

	implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")

	implementation("com.github.Heart-Beats:Downloader:v1.0.1")

	implementation("me.weishu:epic:0.11.0")

	val roomVersion = "2.2.6"
	implementation("androidx.room:room-runtime:$roomVersion")
	annotationProcessor("androidx.room:room-compiler:$roomVersion")
	// optional - Kotlin Extensions and Coroutines support for Room
	implementation("androidx.room:room-ktx:$roomVersion")

	val cameraxVersion = "1.0.0"
	// The following line is optional, as the core library is included indirectly by camera-camera2
	implementation("androidx.camera:camera-core:${cameraxVersion}")
	implementation("androidx.camera:camera-camera2:${cameraxVersion}")
	// If you want to additionally use the CameraX Lifecycle library
	implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
	// If you want to additionally use the CameraX View class
	implementation("androidx.camera:camera-view:1.0.0-alpha24")
	// If you want to additionally use the CameraX Extensions library
	implementation("androidx.camera:camera-extensions:1.0.0-alpha24")

	/*
	*  val versionCode: String by project                // 读取 project 的 gradle.properties 文件定义的 versionCode 变量
	*  val kotlinVersion: String by rootProject.extra   //  读取 rootProject 对应的 build.gradle 文件中定义的 kotlinVersion 变量
	*  val newKotlinVersion: String by extra("1.3.61")  //  在 project 中定义一个新的 newKotlinVersion 变量
	* */
	implementation("com.tencent.shadow.dynamic${project.extra["group_suffix"]}:host:${project.extra["shadow_version"]}")

	implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltVersion"]}")
	kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["hiltVersion"]}")
}