rootProject.name= "application"
rootProject.buildFileName = "build.gradle.kts"

include(":app")
include(":utils", ":uni-sdk", ":method-proxy-library")

includeBuild("../shadow")