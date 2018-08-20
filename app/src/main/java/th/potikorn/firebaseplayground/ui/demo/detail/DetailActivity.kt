package th.potikorn.firebaseplayground.ui.demo.detail

import kotlinx.android.synthetic.main.activity_detail.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.ui.base.BaseActivity

class DetailActivity : BaseActivity() {

    private var repositoryName: String? = null

    override fun layoutToInflate(): Int = R.layout.activity_detail

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {}

    override fun initialize() {
        getIntentData()
        tvRepositoryName.text = repositoryName
    }

    private fun getIntentData() {
        intent?.let {
            repositoryName = it.getStringExtra(EXTRA_REPOS_NAME)
        }
    }

    companion object {
        const val EXTRA_REPOS_NAME = "EXTRA_REPOS_NAME"
    }
}