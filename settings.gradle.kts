rootProject.buildFileName = "build.gradle.kts"

include(":app")

include(":utils", ":uni-sdk", ":method-proxy-library", ":shadow-init")
project(":shadow-init").projectDir = file("shadow/shadow-init")

include(":plugin-lib")
project(":plugin-lib").projectDir = file("shadow/plugin-lib")