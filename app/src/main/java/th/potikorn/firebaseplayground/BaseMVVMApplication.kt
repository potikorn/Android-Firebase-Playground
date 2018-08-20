package th.potikorn.firebaseplayground

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.CsvFormatStrategy
import com.orhanobut.logger.DiskLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import th.potikorn.firebaseplayground.configuration.AppConfigurations
import th.potikorn.firebaseplayground.di.AndroidModule
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.di.DaggerAppComponent
import th.potikorn.firebaseplayground.di.RemoteModule
import th.potikorn.firebaseplayground.di.RetrofitModule

class BaseMVVMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initDependenciesInjection()
        when (AppConfigurations().isDebug()) {
            true -> {
                val formatStrategy = PrettyFormatStrategy
                    .newBuilder()
                    .methodCount(1)
                    .showThreadInfo(false)
                    .build()
                Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
                StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                        .detectAll()
                        .penaltyLog()
                        .build()
                )
                val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog()
                Build.VERSION.SDK_INT.takeIf { it >= Build.VERSION_CODES.HONEYCOMB }?.apply {
                    threadPolicyBuilder.penaltyDeathOnNetwork()
                }
                StrictMode.setThreadPolicy(threadPolicyBuilder.build())
            }
            else -> Logger.addLogAdapter(DiskLogAdapter(CsvFormatStrategy.newBuilder().build()))
        }
    }

    private fun initDependenciesInjection() {
        appComponent = DaggerAppComponent.builder()
            .androidModule(AndroidModule(this))
            .retrofitModule(RetrofitModule())
            .remoteModule(RemoteModule())
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}