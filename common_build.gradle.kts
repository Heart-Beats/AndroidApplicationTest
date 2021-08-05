allprojects {
	repositories {
		commonRepository()
	}
}

fun RepositoryHandler.commonRepository() {
	mavenCentral()
	mavenLocal()
	google()
	maven { url = uri("https://maven.aliyun.com/repository/central") }
	maven { url = uri("https://maven.aliyun.com/repository/jcenter") }
	maven { url = uri("https://maven.aliyun.com/repository/google") }
	maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

	maven { url = uri("https://jitpack.io") }
}



tasks.register("clean", Delete::class) {
	this.delete(rootProject.buildDir)
}