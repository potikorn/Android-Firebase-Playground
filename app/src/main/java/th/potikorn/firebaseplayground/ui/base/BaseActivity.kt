package th.potikorn.firebaseplayground.ui.base

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import th.potikorn.firebaseplayground.BaseMVVMApplication
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.exception.NotSetLayoutException
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), LifecycleOwner {

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @LayoutRes
    protected abstract fun layoutToInflate(): Int

    protected abstract fun doInjection(appComponent: AppComponent)
    protected abstract fun startView()
    protected abstract fun stopView()
    protected abstract fun destroyView()
    protected abstract fun setupInstance()
    protected abstract fun setupView()
    protected abstract fun initialize()

    protected fun setupToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutToInflate() == 0) throw NotSetLayoutException()
        setContentView(layoutToInflate())
        doInjection(BaseMVVMApplication.appComponent)
        setupInstance()
        setupView()
        if (savedInstanceState == null) initialize()
    }

    override fun onStart() {
        super.onStart()
        startView()
    }

    override fun onStop() {
        super.onStop()
        stopView()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified VM : ViewModel> bindViewModel(): VM = ViewModelProviders
        .of(this, viewModelFactory)
        .get(VM::class.java)
}