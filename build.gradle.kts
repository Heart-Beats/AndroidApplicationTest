// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files

        val kotlin_version = "1.4.0"
        classpath(kotlin("gradle-plugin", kotlin_version))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    this.delete(rootProject.buildDir)
}