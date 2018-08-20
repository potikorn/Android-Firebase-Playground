package th.potikorn.firebaseplayground.configuration

interface Config {

    fun isDebug(): Boolean
    fun versionCode(): Int
    fun versionName(): String
    fun deviceType(): String = "android"
    fun endPoint(): String
}