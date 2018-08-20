package th.potikorn.firebaseplayground.configuration

import th.potikorn.firebaseplayground.BuildConfig

class AppConfigurations : Config {
    override fun isDebug(): Boolean = BuildConfig.DEBUG
    override fun versionCode(): Int = BuildConfig.VERSION_CODE
    override fun versionName(): String = BuildConfig.VERSION_NAME
    override fun endPoint(): String = "https://api.github.com/"
}