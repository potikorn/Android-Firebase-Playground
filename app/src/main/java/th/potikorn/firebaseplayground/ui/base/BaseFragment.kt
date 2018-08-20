package th.potikorn.firebaseplayground.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import th.potikorn.firebaseplayground.BaseMVVMApplication
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.exception.NotSetLayoutException
import th.potikorn.firebaseplayground.extensions.inflate
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutToInflate() == 0) throw NotSetLayoutException()
        doInjection(BaseMVVMApplication.appComponent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        container?.inflate(layoutToInflate())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyView()
    }

    @Suppress("PROTECTED_CALL_FROM_PUBLIC_INLINE")
    inline fun <reified VM : ViewModel> bindViewModel(): VM = ViewModelProviders
        .of(this, viewModelFactory)
        .get(VM::class.java)
}