package th.potikorn.firebaseplayground.ui.adapter.githublist

import th.potikorn.firebaseplayground.ui.base.adapter.BaseListAdapterPresenter

class GithubAdapterPresenter : BaseListAdapterPresenter<GithubAdapterContract.Adapter>(),
    GithubAdapterContract.Presenter {

    companion object {
        fun create() = GithubAdapterPresenter()
    }
}