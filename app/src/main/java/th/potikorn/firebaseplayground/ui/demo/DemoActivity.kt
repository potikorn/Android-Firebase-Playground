package th.potikorn.firebaseplayground.ui.demo

import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.addFragment
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.demo.fragment.DemoListFragment

class DemoActivity : BaseActivity() {

    override fun layoutToInflate(): Int = R.layout.activity_demo

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        addFragment(DemoListFragment.newInstance(), R.id.frameContainer)
    }

    override fun initialize() {}
}