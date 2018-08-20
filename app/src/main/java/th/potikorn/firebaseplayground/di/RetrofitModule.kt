package th.potikorn.firebaseplayground.di

import com.google.gson.Gson
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import th.potikorn.firebaseplayground.configuration.Config
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(config: Config): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                LoggingInterceptor.Builder()
                    .loggable(config.isDebug())
                    .setLevel(Level.BODY)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .addHeader("version", config.versionName())
                    .build()
            )
            .certificatePinner(CertificatePinner.DEFAULT)
            .connectTimeout(TIMED_OUT, TimeUnit.SECONDS)
            .readTimeout(TIMED_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIMED_OUT, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(config: Config, gson: Gson, httpClient: OkHttpClient) =
        Retrofit.Builder()
            .baseUrl(config.endPoint())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()

    companion object {
        const val TIMED_OUT = 60L
    }
}