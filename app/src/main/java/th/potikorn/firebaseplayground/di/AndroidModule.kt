package th.potikorn.firebaseplayground.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import th.potikorn.firebaseplayground.BaseMVVMApplication
import th.potikorn.firebaseplayground.configuration.AppConfigurations
import th.potikorn.firebaseplayground.configuration.Config
import javax.inject.Singleton

@Module
class AndroidModule(val application: BaseMVVMApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext() = application

    @Provides
    @Singleton
    fun provideConfig(): Config = AppConfigurations()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()
}