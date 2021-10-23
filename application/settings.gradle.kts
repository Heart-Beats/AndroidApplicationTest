rootProject.name= "application"
rootProject.buildFileName = "build.gradle.kts"

include(":app")
include(":utils", ":uni-sdk", ":method-proxy-library")
include(":api")
include(":nativelib")

includeBuild("../shadow")
includeBuild("../uikit")