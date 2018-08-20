package th.potikorn.firebaseplayground.ui.demo.fragment

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_demo_list.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.ui.adapter.githublist.GithubAdapter
import th.potikorn.firebaseplayground.ui.base.BaseFragment
import th.potikorn.firebaseplayground.ui.viewmodel.GithubProjectViewModel

class DemoListFragment : BaseFragment() {

    private val githubProjectViewModel: GithubProjectViewModel by lazy { bindViewModel<GithubProjectViewModel>() }

    private val githubProjectAdapter: GithubAdapter by lazy { GithubAdapter() }

    override fun layoutToInflate(): Int = R.layout.fragment_demo_list

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        rvSimpleList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = githubProjectAdapter
        }
    }

    override fun initialize() {
        githubProjectViewModel.liveGithubProject.observe(this, Observer {
            githubProjectAdapter.setItems(it)
        })
        githubProjectViewModel.getProjectListByUserName("20Scoops-CNX")
    }

    companion object {
        fun newInstance(): DemoListFragment {
            return DemoListFragment()
        }
    }
}