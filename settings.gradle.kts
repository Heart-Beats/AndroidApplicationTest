// addShadow()
//
// fun addShadow() {
// 	include(":shadow-init")
// 	include(":plugin-aidl")
// 	include(":plugin-manager")
// 	project(":shadow-init").projectDir = file("shadow/shadow-init")
// 	project(":plugin-aidl").projectDir = file("shadow/plugin-aidl")
// 	project(":plugin-manager").projectDir = file("shadow/plugin-manager")
// }

includeBuild("./application")
includeBuild("./shadow")